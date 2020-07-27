package com.rjgconfeccoes.ui.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.PedidosViewHolder> {

    @NonNull
    @Override
    public PedidosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PedidosViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PedidosViewHolder extends RecyclerView.ViewHolder{

        public PedidosViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
