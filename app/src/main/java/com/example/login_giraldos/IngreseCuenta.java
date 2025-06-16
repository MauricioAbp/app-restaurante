
package com.example.login_giraldos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.util.Log;
import org.json.JSONObject;
import com.loopj.android.http.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.BreakIterator;

import cz.msebera.android.httpclient.Header;

import android.util.Log;
import org.json.JSONObject;
import com.loopj.android.http.*;

public class IngreseCuenta extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnRegresar;
    Button btnEntrar;
    EditText edCorreo, edContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingrese_cuenta);

        btnRegresar = findViewById(R.id.btnIrAMain);
        btnEntrar = findViewById(R.id.btnIngresar);
        edCorreo = findViewById(R.id.edtCorreo);
        edContrasena = findViewById(R.id.edtContraseña);

        btnRegresar.setOnClickListener(this);
        btnEntrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegresar) {
            startActivity(new Intent(IngreseCuenta.this, Inicio.class));
        }

        if (v == btnEntrar) {
            String correo = edCorreo.getText().toString().trim();
            String contrasena = edContrasena.getText().toString().trim();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enviar solicitud al servidor
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("correo", correo);
            params.put("contrasena", contrasena);

            String url = "http://10.0.2.2/PHP_CONEXIONES/login.php";

            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody);
                        JSONObject json = new JSONObject(response);
                        boolean success = json.getBoolean("success");
                        String message = json.getString("message");

                        Toast.makeText(IngreseCuenta.this, message, Toast.LENGTH_SHORT).show();

                        if (success) {
                            Intent intent = new Intent(IngreseCuenta.this, MenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        Log.e("LoginParseError", e.getMessage());
                        Toast.makeText(IngreseCuenta.this, "Error al interpretar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(IngreseCuenta.this, "Error de conexión: " + statusCode, Toast.LENGTH_SHORT).show();
                    Log.e("LoginFail", "Error: " + error.getMessage());
                }
            });
        }
    }
}
