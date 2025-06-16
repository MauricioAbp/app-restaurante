package com.example.login_giraldos;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerMenu;
    private MenuAdapter menuAdapter;
    ImageButton btnVerDetalle, Delivery;

    private TextView tvResumenPedido, tvCostoTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnConfirmar = findViewById(R.id.btnConfirmar);
        Button btnEliminarTodo = findViewById(R.id.btnEliminarTodo);

        btnConfirmar.setOnClickListener(v -> confirmarPedido());
        btnEliminarTodo.setOnClickListener(v -> eliminarTodoPedido());


        btnVerDetalle = findViewById(R.id.btnVerDetalle);
        btnVerDetalle.setOnClickListener(this);


        recyclerMenu = findViewById(R.id.recyclerMenu);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this));

        Delivery = findViewById(R.id.btnIrAMain);
        Delivery.setOnClickListener(this);

        tvResumenPedido = findViewById(R.id.tvResumenPedido);
        tvCostoTotal = findViewById(R.id.tvCostoTotal);
        Button btnPerfil = findViewById(R.id.btnPerfil);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, Perfil.class);
                startActivity(intent);
            }
        });


        List<MenuItem> lista = new ArrayList<>();
        lista.add(new MenuItem("1/8 de Pollo", "Incluye papas + ensalada", 9.90, R.drawable.acceso));
        lista.add(new MenuItem("1/4 de Pollo", "Incluye papas + ensalada", 14.00, R.drawable.acceso));
        lista.add(new MenuItem("1/2 Pollo", "Incluye papas + ensalada", 22.00, R.drawable.acceso));
        lista.add(new MenuItem("1 Pollo", "Incluye papas + ensalada", 44.00, R.drawable.acceso));

        menuAdapter = new MenuAdapter(lista, this::actualizarResumen);
        recyclerMenu.setAdapter(menuAdapter);
    }

    // Método para actualizar el resumen del pedido
    private void actualizarResumen(List<MenuItem> seleccionados) {
        if (seleccionados.isEmpty()) {
            tvResumenPedido.setText("Pedido:");
            tvCostoTotal.setText("Total: S/. 0.00");
            return;
        }

        StringBuilder resumen = new StringBuilder("Pedido:\n");
        double total = 0;
        for (MenuItem item : seleccionados) {
            resumen.append("- ").append(item.getNombre()).append("\n");
            total += item.getPrecio();
        }
        tvResumenPedido.setText(resumen.toString());
        tvCostoTotal.setText(String.format("Total: S/. %.2f", total));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnIrAMain) {
            List<MenuItem> seleccionados = menuAdapter.getSelectedItems();
            if (seleccionados.isEmpty()) {
                Toast.makeText(this, "Seleccione al menos un pedido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Aquí puedes enviar datos o seguir con la siguiente actividad
            Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
            // Si quieres enviar pedidos seleccionados, usa intent.putExtra aquí
            startActivity(intent);
        }
        else if (view.getId() == R.id.btnVerDetalle) {
            List<MenuItem> seleccionados = menuAdapter.getSelectedItems();

            if (seleccionados.isEmpty()) {
                Toast.makeText(this, "Seleccione al menos un pedido", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder resumen = new StringBuilder();
            double total = 0;

            for (MenuItem item : seleccionados) {
                resumen.append("- ").append(item.getNombre())
                        .append(" S/. ").append(String.format("%.2f", item.getPrecio()))
                        .append("\n");
                total += item.getPrecio();
            }

            Intent intent = new Intent(MenuActivity.this, DetalleActivity.class);
            intent.putExtra("resumen", resumen.toString());
            intent.putExtra("total", total);
            startActivity(intent);

        }

    }
    private void confirmarPedido() {
        List<MenuItem> seleccionados = menuAdapter.getSelectedItems();
        if (seleccionados.isEmpty()) {
            Toast.makeText(this, "No hay productos para confirmar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Solo insertaremos el primer plato para generar el pedido
        MenuItem primerPlato = seleccionados.get(0);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("nombre", primerPlato.getNombre());
        params.put("precio", primerPlato.getPrecio());
        params.put("descripcion", primerPlato.getDescripcion());

        client.post("http://10.0.2.2/PHP_CONEXIONES/guardar_pedido.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        int idPedido = response.getInt("id_pedido");

                        // Luego enviamos todos los detalles del pedido con ese idPedido
                        enviarDetallePedido(idPedido, seleccionados);
                        Toast.makeText(MenuActivity.this, "Pedido confirmado y enviado.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MenuActivity.this, "Error al crear pedido", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(MenuActivity.this, "Error de conexión al crear pedido", Toast.LENGTH_SHORT).show();
            }
        });

        menuAdapter.clearSeleccionados();
        actualizarResumen(new ArrayList<>());
    }


    private void eliminarTodoPedido() {
        List<MenuItem> seleccionados = menuAdapter.getSelectedItems();
        if (seleccionados.isEmpty()) {
            Toast.makeText(this, "No hay productos para eliminar.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (MenuItem item : seleccionados) {
            eliminarPedidoDeBaseDeDatos(item);
        }

        Toast.makeText(this, "Pedido eliminado.", Toast.LENGTH_SHORT).show();
        menuAdapter.clearSeleccionados();
        actualizarResumen(new ArrayList<>());
    }
    private void enviarPedidoABaseDeDatos(MenuItem item) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("nombre", item.getNombre());
        params.put("precio", item.getPrecio());
        params.put("descripcion", item.getDescripcion());

        client.post("http://10.0.2.2/PHP_CONEXIONES/guardar_pedido.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Pedido", "✅ Enviado a la BD");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Pedido", "❌ Error al enviar a la BD");
            }
        });
    }

    private void eliminarPedidoDeBaseDeDatos(MenuItem item) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("nombre", item.getNombre());

        client.post("http://10.0.2.2/PHP_CONEXIONES/eliminar_pedido.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Eliminar", "✅ Pedido eliminado de la BD");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Eliminar", "❌ Error al eliminar de la BD");
            }
        });
    }
    private void enviarDetallePedido(int idPedido, List<MenuItem> listaDePedidos) {
        JSONArray detalleArray = new JSONArray();

        try {
            for (MenuItem item : listaDePedidos) {
                JSONObject obj = new JSONObject();
                obj.put("id_plato", item.getNombre()); // o item.getId() si tienes uno
                obj.put("cantidad", 1); // cambia si tienes control de cantidad
                obj.put("subtotal", item.getPrecio());
                detalleArray.put(obj);
            }

            RequestParams params = new RequestParams();
            params.put("id_pedido", idPedido);
            params.put("detalle", detalleArray.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://10.0.2.2/PHP_CONEXIONES/guardar_pedido_detalle.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d("Detalle", "✅ Detalles enviados");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("Detalle", "❌ Error al enviar detalles");
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
