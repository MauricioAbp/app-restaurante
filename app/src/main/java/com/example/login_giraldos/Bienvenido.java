package com.example.login_giraldos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Bienvenido extends AppCompatActivity {
    //Se establece el tiempo de carga del Splash
    public static int tiempodecarga=2000;
    @Override

    //Se crean las instancias correspondientes
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //Se hace referencia al activity donde se esta trabajando
        setContentView(R.layout.activity_bienvenido);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){

                //Se enlaza la clase activity donde se quiere ir: Bienvenido -> Inicio)
                Intent intent = new Intent(Bienvenido.this, Inicio.class);
                startActivity(intent);
                finish();
            }

        }, tiempodecarga);
    }
}