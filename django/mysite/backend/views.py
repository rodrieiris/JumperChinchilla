from django.http import HttpResponse, HttpResponseRedirect
from django.template import loader
from django.shortcuts import get_object_or_404, render
from django.http import HttpResponseNotAllowed, JsonResponse
from .models import Score

def mostrarWebscore (request):
    if request.method == "GET":
        puntuaciones = Score.objects.all()
        score = []
        for puntuacion in puntuaciones:
            array_score =  {
                'nickname': puntuacion.nickname, 
                'score': puntuacion.puntuacion, 
                'date': puntuacion.fecha_creacion
                }
            score.append(array_score)
        return HttpResponse(score, status=200)
    else:
        return HttpResponseNotAllowed(['GET'])

def index(request):
    ranking = Score.objects.order_by("-puntuacion")
    template = loader.get_template('backend/index.html')
    context = {
        'ranking': ranking,
    }
    return HttpResponse(template.render(context, request))