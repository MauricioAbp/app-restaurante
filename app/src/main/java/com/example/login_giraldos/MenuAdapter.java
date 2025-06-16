package com.example.login_giraldos;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuItems;
    private Set<Integer> selectedItems = new HashSet<>();
    private Consumer<List<MenuItem>> seleccionListener; // Callback al seleccionar

    // Constructor con listener
    public MenuAdapter(List<MenuItem> menuItems, Consumer<List<MenuItem>> seleccionListener) {
        this.menuItems = menuItems;
        this.seleccionListener = seleccionListener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuItems.get(position);
        holder.nombre.setText(item.getNombre());
        holder.descripcion.setText(item.getDescripcion());
        holder.precio.setText(String.format("S/. %.2f", item.getPrecio()));
        holder.imagen.setImageResource(item.getImagenResId());

        // Color de fondo según si está seleccionado
        holder.itemView.setBackgroundColor(
                selectedItems.contains(position) ? Color.LTGRAY : Color.WHITE
        );

        // Manejo de selección
        holder.itemView.setOnClickListener(v -> {
            if (selectedItems.contains(position)) {
                selectedItems.remove(position);
            } else {
                selectedItems.add(position);
            }
            notifyItemChanged(position);

            if (seleccionListener != null) {
                seleccionListener.accept(getSelectedItems());
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    // Devuelve los ítems seleccionados
    public List<MenuItem> getSelectedItems() {
        List<MenuItem> seleccionados = new ArrayList<>();
        for (Integer pos : selectedItems) {
            seleccionados.add(menuItems.get(pos));
        }
        return seleccionados;
    }
    // Limpia la selección actual
    public void clearSelectedItems() {
        selectedItems.clear();
        notifyDataSetChanged();
        if (seleccionListener != null) {
            seleccionListener.accept(getSelectedItems());
        }
    }
    public void clearSeleccionados() {
        selectedItems.clear(); // Limpia el conjunto de seleccionados
        notifyDataSetChanged(); // Actualiza el RecyclerView
        if (seleccionListener != null) {
            seleccionListener.accept(getSelectedItems()); // Refresca el resumen
        }
    }



    // ViewHolder
    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, precio;
        ImageView imagen;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombre);
            descripcion = itemView.findViewById(R.id.txtDescripcion);
            precio = itemView.findViewById(R.id.txtPrecio);
            imagen = itemView.findViewById(R.id.imgPlato);
        }
    }
}
