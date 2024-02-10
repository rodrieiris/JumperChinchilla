from .models import Score
from django.http import HttpResponseNotAllowed, JsonResponse
import json
from django.views.decorators.csrf import csrf_exempt
@csrf_exempt

def mostrarscore (request):
    if request.method == "GET":
        longitud = request.GET.get("length")
        puntuaciones = Score.objects.order_by('-fecha_creacion')[:longitud]
        score = []
        for puntuacion in puntuaciones:
            array_score =  {'nickname': puntuacion.nickname, 'score': puntuacion.puntuacion, 'date': puntuacion.fecha_creacion}
            score.append(array_score)
        return JsonResponse(score, safe=False, status=200)

    elif request.method == "POST":
        request_body = json.loads(request.body)
        name_in_request = request_body.get('name')
        score_in_request = request_body.get('score')
        score = Score(nickname=name_in_request, puntuacion=score_in_request)
        score.save()
        return JsonResponse({"id":score.id}, status = 201)
    else:
        return HttpResponseNotAllowed(['GET', 'POST'])

def recibirnickname (request, nick, score):
    if request.method == "GET":
        if nick == nickname.Score:
            listanickname = Score.objects.filter(nickname==nick)
            score = []
            for nickname in listanickname:
                array_score =  {'nickname': nickname.nickname, 'score': nickname.puntuacion, 'date': nickname.fecha_creacion}
                score.append(array_score)
            return JsonResponse(score, safe=False, status=200)
        else:
            return print ("No existe ningun usuario con ese nombre")
