package com.example.login_giraldos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Perfil extends AppCompatActivity implements View.OnClickListener {
    ImageButton RegresarInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        RegresarInicio=findViewById(R.id.btnIrAMain);
        RegresarInicio.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (RegresarInicio == v) {
            Intent intent = new Intent(Perfil.this, MenuActivity.class);
            startActivity(intent); // ¡Esto es lo que faltaba!
        }
    }

}