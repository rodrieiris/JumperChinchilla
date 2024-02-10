import datetime
from django.db import models
from django.utils import timezone

class Score(models.Model):
	nickname = models.CharField(max_length=9)
	puntuacion = models.IntegerField(default=0)
	fecha_creacion = models.DateTimeField(auto_now_add=True)
	def __str__(self):
		return self.nickname
	def puntuaciones(self):
		return self.puntuaciones
