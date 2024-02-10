package com.example.jumperchinchilla;

public class Score{
    String nickname = null;
    int puntuacion = 0;
    String fecha_creacion = null;

    public Score(String nickname, int puntuacion, String fecha_creacion) {
        this.nickname = nickname;
        this.puntuacion = puntuacion;
        this.fecha_creacion = fecha_creacion;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }
}