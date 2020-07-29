package com.rjgconfeccoes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Pedidos;
import com.rjgconfeccoes.ui.util.Base64Custom;

import java.util.ArrayList;

public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.PedidosViewHolder> {

    private final Context context;
    private final ArrayList<Pedidos> listaPedidos;

    public AdapterPedidos(Context context, ArrayList<Pedidos> listaPedidos) {
        this.context = context;
        this.listaPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public PedidosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.lst_pedidos_fragment, parent, false);
        return new PedidosViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidosViewHolder holder, int position) {
        Pedidos pedidos = listaPedidos.get(position);
        holder.vincula(pedidos);

    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public class PedidosViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardViewItem;
        private final TextView descPedido;
        private final TextView nomeCliente;
        private final TextView precoTotalPedido;

        public PedidosViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewItem = itemView.findViewById(R.id.cardViewItem);
            descPedido = itemView.findViewById(R.id.tv_desc_pedido_pedidoFragment);
            nomeCliente = itemView.findViewById(R.id.tv_cliente_pedidoFragment);
            precoTotalPedido = itemView.findViewById(R.id.tv_preco_total_pedido_pedidoFragment);

        }

        public void vincula(Pedidos pedidos) {
            String decodificaNomeCliente = Base64Custom.decodificarStringBase64(pedidos.getClienteId());
            String[] identificadores = pedidos.getId().split(";");

            descPedido.setText("Pedido: " + identificadores[2]);
            nomeCliente.setText("Cliente: " + decodificaNomeCliente);
//            precoTotalPedido.setText("R$: " + Util.formataPreco(Double.parseDouble(pedidos.getValorTotalPedido())));

        }
    }
}
