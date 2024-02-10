package com.example.jumperchinchilla;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static TextView txt_score, txt_best_score, txt_score_over;
    public static RelativeLayout rl_game_over;
    public static Button btn_start;
    private GameView gv;
    private EditText editTextAftergameName;
    private MediaPlayer mediaPlayer;
    public ImageButton muteButton;
    public ImageButton scoreButton;
    private int cont = 1;
    private RecyclerView recyclerView;

    private static String NombrePreferences = "JUMPER_PREFS";
    private static String PrefsKeyUsuario = "USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;

        setContentView(R.layout.activity_main);
        txt_score = findViewById(R.id.txt_score);
        txt_best_score = findViewById(R.id.txt_best_score);
        txt_score_over = findViewById(R.id.txt_score_over);
        rl_game_over = findViewById(R.id.rl_game_over);
        btn_start = findViewById(R.id.btn_start);
        editTextAftergameName = findViewById(R.id.editTextTextPersonName);
        gv = findViewById(R.id.gv);
        muteButton = findViewById(R.id.mute);
        scoreButton = findViewById(R.id.score);

        mediaPlayer = mediaPlayer.create(this, R.raw.sillychipsong);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv.setStart(true);
                txt_score.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.INVISIBLE);
                muteButton.setVisibility(View.INVISIBLE);
                scoreButton.setVisibility(View.INVISIBLE);
            }
        });
        rl_game_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences preferences = getSharedPreferences(NombrePreferences, Context.MODE_PRIVATE);
                //String nombreUsuario = preferences.getString(PrefsKeyUsuario, "NombreUsuario333");
                //editTextAftergameName.setText(nombreUsuario);
                publicarPuntuacionYVolverAEmpezar();
            }
        });

        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (cont) {
                    case 0:
                        try {
                            mediaPlayer.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        cont++;
                        break;
                    case 1:
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            cont=0;
                        }
                        break;
                }
            }
        });

        scoreButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //setContentView(R.layout.scoreboard);
            //}

            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://192.168.0.88:8000/index");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        });

    }

    private void publicarPuntuacionYVolverAEmpezar() {
        AlertDialog alertaCargando = crearAlertaCargando();
        alertaCargando.show();
        JSONObject cuerpoPeticion = generarJsonAftergame();
        JsonObjectRequest laPeticionQueVoyAMandar = new JsonObjectRequest(
                Request.Method.POST,
                Cliente.serverURL + "/score",
                cuerpoPeticion,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        alertaCargando.hide();
                        restaurarVistaJuegoTrasTerminar();
                        SharedPreferences.Editor editor = getSharedPreferences(NombrePreferences, Context.MODE_PRIVATE).edit();
                        editor.putString(PrefsKeyUsuario, editTextAftergameName.getText().toString());
                        System.out.println("Voy a guardar en prefs " + editTextAftergameName.getText().toString());
                        editor.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        alertaCargando.hide();
                        crearAlertaError().show();
                        error.printStackTrace();
                        restaurarVistaJuegoTrasTerminar();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(laPeticionQueVoyAMandar);
    }

    private JSONObject generarJsonAftergame() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", editTextAftergameName.getText().toString());
            jsonObject.put("score", Integer.parseInt(txt_score.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private AlertDialog crearAlertaCargando() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Publicando puntuación...");
        builder.setCancelable(false);
        return builder.create();
    }

    private AlertDialog crearAlertaError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tu puntuación no se ha publicado. ¡Lo sentimos!");
        builder.setPositiveButton("No pasa nada", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // To-Do: A lo mejor reintentar?
            }
        });
        return builder.create();
    }

    private void restaurarVistaJuegoTrasTerminar() {
        btn_start.setVisibility(View.VISIBLE);
        muteButton.setVisibility(View.VISIBLE);
        scoreButton.setVisibility(View.VISIBLE);
        rl_game_over.setVisibility(View.INVISIBLE);
        gv.setStart(false);
        gv.reset();
    }
}