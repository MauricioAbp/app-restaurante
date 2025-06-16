package com.example.login_giraldos;

public class MenuItem {
    private String nombre;
    private String descripcion;
    private double precio;
    private int imagenResId;

    // Estas son las variables que estaban faltando:
    private int idPlato;
    private int cantidad;

    public MenuItem(String nombre, String descripcion, double precio, int imagenResId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenResId = imagenResId;

        // Por defecto puedes asignar valores
        this.idPlato = nombre.hashCode(); // o cualquier lógica para generar ID
        this.cantidad = 1; // cantidad mínima inicial
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return precio * cantidad;
    }

    // Puedes agregar setters si necesitas cajmbiar valores después:
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setIdPlato(int idPlato) {
        this.idPlato = idPlato;
    }
}
