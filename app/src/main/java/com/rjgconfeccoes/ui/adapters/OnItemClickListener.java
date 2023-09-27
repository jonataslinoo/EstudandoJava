package com.rjgconfeccoes.ui.adapters;

import com.rjgconfeccoes.model.Pedidos;

public interface OnItemClickListener {

    void onItemLongClickListener(int position, Pedidos pedidos);
    void onItemClickListener(int position, Pedidos pedidos);
}
