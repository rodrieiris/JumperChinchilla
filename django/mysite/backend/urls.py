from django.urls import include, path
from . import views
from . import rest_facade

app_name = 'backend'
urlpatterns = [
    path('mostrarwebscore', views.mostrarWebscore, name= "mostrarWebscore"),
    path('score', rest_facade.mostrarscore, name="mostrarscore"),
    path('index', views.index, name='index'),
]