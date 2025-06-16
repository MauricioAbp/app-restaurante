package com.example.login_giraldos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;


public class MetodoPagos extends AppCompatActivity {

    RadioGroup radioGroup;
    Button btnPagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodo_pagos);

        radioGroup = findViewById(R.id.radioGroup);
        btnPagar = findViewById(R.id.btnPagar);

        btnPagar.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId != -1) {
                RadioButton selectedRadio = findViewById(selectedId);
                String metodoPago = selectedRadio.getText().toString();

                Toast.makeText(MetodoPagos.this,
                        "Has seleccionado: " + metodoPago,
                        Toast.LENGTH_SHORT).show();
                enviarMetodoPago(metodoPago);


                // Aquí podrías enviar los datos a tu base de datos o servidor
                // enviarMetodoPago(metodoPago);

            } else {
                Toast.makeText(MetodoPagos.this,
                        "Selecciona un método de pago", Toast.LENGTH_SHORT).show();
            }
            
        });
        
    }

    private void enviarMetodoPago(String metodoPago) {
        String URL = "http://10.0.2.2/guardar_pago.php"; // o la IP de tu servidor

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(MetodoPagos.this, "Pago registrado", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(MetodoPagos.this, "Error al guardar pago", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("metodo_pago", metodoPago);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
