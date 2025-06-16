package com.example.login_giraldos;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Inicio extends AppCompatActivity {
    //Se crea el boton Login
    Button Login;
    TextView Regitro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.inicio);
        Login = findViewById(R.id.btnRegistrar);
        TextView Registro = findViewById(R.id.btnTextRegistrarse);
        Registro.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Inicio.this, Registrar.class);
                startActivity(intent1);
            }
        });
        //Se crea un evento para el boton Login
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            //Se crea el evento, al presionar el boton Login nos transportaremos a la actividad MenuActivity.
            public void onClick(View v) {
                Intent intent2 = new Intent(Inicio.this, IngreseCuenta.class);
                startActivity(intent2);
            }

        });
    }
}
