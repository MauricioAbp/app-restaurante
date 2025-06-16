package com.example.login_giraldos;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.widget.ImageButton;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class Registrar extends AppCompatActivity {
    EditText etNombre, etApellido, etFecha, etCorreo, etContrasena;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);

        ImageButton btnIrAMain = findViewById(R.id.btnIrAMain);

        btnIrAMain.setOnClickListener(v -> {
            Intent intent = new Intent(Registrar.this, Inicio.class);
            startActivity(intent);
        });

        // Enlazamos los componentes
        etNombre = findViewById(R.id.pTxNombre);
        etApellido = findViewById(R.id.pTxApellido);
        etFecha = findViewById(R.id.pTxFechaNacimiento);
        etCorreo = findViewById(R.id.pTxCorreo);
        etContrasena = findViewById(R.id.pTxContraseña);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(view -> {
            String nombre = etNombre.getText().toString().trim();
            String apellido = etApellido.getText().toString().trim();
            String fecha = etFecha.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || fecha.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enviar datos al servidor
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("nombre", nombre);
            params.put("apellido", apellido);
            params.put("fecha_nacimiento", fecha);
            params.put("correo", correo);
            params.put("contrasena", contrasena);

            String url = "http://10.0.2.2/PHP_CONEXIONES/registrar.php";

            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d("RegistrarPHP", "✅ Éxito - Código: " + statusCode);
                    Log.d("RegistrarPHP", "Respuesta del servidor: " + response);
                    Toast.makeText(Registrar.this, response, Toast.LENGTH_LONG).show();

                    limpiarCampos();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("RegistrarError", "❌ Fallo - Código: " + statusCode);
                    if (responseBody != null) {
                        String errorResponse = new String(responseBody);
                        Log.e("RegistrarError", "Respuesta del servidor: " + errorResponse);
                    } else {
                        Log.e("RegistrarError", "Error sin cuerpo de respuesta");
                    }
                    Log.e("RegistrarError", "Excepción: " + error.getMessage());
                    Toast.makeText(Registrar.this, "Error al registrar: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etApellido.setText("");
        etFecha.setText("");
        etCorreo.setText("");
        etContrasena.setText("");
    }
}
