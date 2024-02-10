package com.example.jumperchinchilla;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ScoreDto {
    private List<Score> Score;

    public ScoreDto(JSONArray jsonArray) {
        try {
            this.Score = new ArrayList<>();
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject ScoreJSONDeLaLista = jsonArray.getJSONObject(i);
                String newNickname = ScoreJSONDeLaLista.getString("nickname");
                int newScore = ScoreJSONDeLaLista.getInt("score");
                String newDate = ScoreJSONDeLaLista.getString("date");
                Score puntuacion = new Score(newNickname, newScore, newDate);
                this.Score.add(puntuacion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            this.Score = new ArrayList<Score>();
        }
    }

    public List<Score> getpuntuacion() {
        return this.Score;
    }
}