package com.example.login_giraldos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;


public class DetalleActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvResumenDetalle, tvTotalDetalle;
    ImageButton flecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_activity);

        tvResumenDetalle = findViewById(R.id.tvResumenDetalle);
        tvTotalDetalle = findViewById(R.id.tvTotalDetalle);

        String resumen = getIntent().getStringExtra("resumen");
        double total = getIntent().getDoubleExtra("total", 0.0);

        tvResumenDetalle.setText(resumen);
        tvTotalDetalle.setText(String.format("Total: S/. %.2f", total));

        flecha = findViewById(R.id.btnIrAMain);
        flecha.setOnClickListener(this);

    }
    private void enviarDetalleABaseDatos(String resumen, double total) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://10.0.2.2/PHP_CONEXIONES/RegistrarDetalle.php";

        RequestParams params = new RequestParams();
        params.put("resumen", resumen);
        params.put("total", total);

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(DetalleActivity.this, "Detalle registrado con éxito", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(DetalleActivity.this, "Error al registrar detalle", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(flecha==v){
            Intent intent = new Intent(DetalleActivity.this,MenuActivity.class);
            startActivity(intent);
        }
    }
}
