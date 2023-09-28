package com.rjgconfeccoes.ui.adapters;

import com.rjgconfeccoes.model.Pedido;

public interface OnItemClickListener {

    void onItemLongClickListener(int position, Pedido pedido);
    void onItemClickListener(int position, Pedido pedido);
}
