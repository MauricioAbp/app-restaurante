package com.example.login_giraldos;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    private Marker currentMarker;
    private Button btnIrMetodoPago;


    private static final int REQUEST_LOCATION_PERMISSION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnIrMetodoPago = findViewById(R.id.btnIrMetodoPago);
        btnIrMetodoPago.setOnClickListener(v -> {
            Intent intent = new Intent(MapsActivity.this, MetodoPagos.class);
            startActivity(intent);
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mostrarUbicacionActual();
        }

        // 🔴 Captura clics y envía ubicación
        mMap.setOnMapClickListener(latLng -> {
            if (currentMarker != null) {
                currentMarker.remove();
            }
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            mostrarDireccion(latLng);
            enviarUbicacionALaBaseDeDatos(latLng.latitude, latLng.longitude); // ⬅ Aquí se envía
        });
    }

    private void mostrarUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
            }
        });
    }

    private void mostrarDireccion(LatLng latLng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!addresses.isEmpty()) {
                String direccion = addresses.get(0).getAddressLine(0);
                Toast.makeText(this, "Dirección seleccionada: " + direccion, Toast.LENGTH_LONG).show();

                // Mostrar el botón
                btnIrMetodoPago.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se pudo obtener la dirección", Toast.LENGTH_SHORT).show();
        }
    }

    // ✅ Envío a la base de datos
    private void enviarUbicacionALaBaseDeDatos(double latitud, double longitud) {
        String URL = "http://10.0.2.2/localizacion.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(MapsActivity.this, "Ubicación enviada", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(MapsActivity.this, "Error al enviar ubicación", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitud", String.valueOf(latitud));
                params.put("longitud", String.valueOf(longitud));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

}
