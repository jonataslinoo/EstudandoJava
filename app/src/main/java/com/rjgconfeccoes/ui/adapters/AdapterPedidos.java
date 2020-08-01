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
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Pedidos;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.model.ProdutoPedido;
import com.rjgconfeccoes.ui.util.Base64Custom;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.PedidosViewHolder> {

    private final Context context;
    private final ArrayList<Pedidos> listaPedidos;
    private OnItemClickListener onItemClickListener;
    private double valorTotal;
    private int quantidadeItens;

    public AdapterPedidos(Context context, ArrayList<Pedidos> listaPedidos) {
        this.context = context;
        this.listaPedidos = listaPedidos;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

        holder.cardViewItem.setOnLongClickListener(view -> {
            onItemClickListener.onItemLongClickListener(position, pedidos);
            return false;
        });

        holder.cardViewItem.setOnClickListener(view -> onItemClickListener.onItemClickListener(position, pedidos));
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
        private final TextView quantidadeItensPedido;
        private final TextView dataPedido;
        private final TextView dataPedidoFinalizado;
        private final TextView pedidoFinalizado;

        public PedidosViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewItem = itemView.findViewById(R.id.cardViewItem);
            descPedido = itemView.findViewById(R.id.tv_desc_pedido_pedidoFragment);
            nomeCliente = itemView.findViewById(R.id.tv_cliente_pedidoFragment);
            precoTotalPedido = itemView.findViewById(R.id.tv_preco_total_pedido_pedidoFragment);
            quantidadeItensPedido = itemView.findViewById(R.id.tv_quantidade_itens_pedido_pedidoFragment);
            dataPedido = itemView.findViewById(R.id.tv_data_pedido_pedidoFragment);
            dataPedidoFinalizado = itemView.findViewById(R.id.tv_data_pedido_finalizado_pedidoFragment);
            pedidoFinalizado = itemView.findViewById(R.id.tv_pedido_finalizado_pedidoFragment);

//            itemView.setOnLongClickListener(view -> {
//                onItemClickListener.onItemLongClickListener(getAdapterPosition(), pedidos);
//                return false;
//            });
//
//            itemView.setOnClickListener(view -> onItemClickListener.onItemClickListener(getAdapterPosition(), pedidos));
        }

        public void vincula(Pedidos pedidos) {
            retornaValorPedido(pedidos);
            String decodificaNomeCliente = Base64Custom.decodificarStringBase64(pedidos.getClienteId());
            String[] identificadores = pedidos.getId().split(";");

            descPedido.setText("Pedido: " + identificadores[1]);
            nomeCliente.setText("Cliente: " + decodificaNomeCliente);
            dataPedido.setText(Util.converteDataHorasSegundos(Long.parseLong(identificadores[1])));

            if (quantidadeItens > 1) {
                quantidadeItensPedido.setText(quantidadeItens + " Itens");
            } else {
                quantidadeItensPedido.setText(quantidadeItens + " Item");
            }

            precoTotalPedido.setText("R$: " + Util.formataPreco(valorTotal));

            if (identificadores.length > 2) {
                pedidoFinalizado.setVisibility(View.VISIBLE);
                pedidoFinalizado.setText("Finalizado");
                dataPedidoFinalizado.setVisibility(View.VISIBLE);
                dataPedidoFinalizado.setText(Util.converteDataHorasSegundos(Long.parseLong(identificadores[2])));
            } else {
                dataPedidoFinalizado.setVisibility(View.GONE);
                pedidoFinalizado.setVisibility(View.GONE);
            }
        }
    }

    private void retornaValorPedido(Pedidos pedidos) {
        Dados dados = Util.recuperaDados();

        valorTotal = 0.0;
        quantidadeItens = 0;

        for (ProdutoPedido produtoPedido : pedidos.getListaProdutosPedido()) {
            for (Produto produto : dados.obtemListaProdutos()) {
                if (produtoPedido.getProdutoId().equals(produto.getDescricao())) {
                    valorTotal += (produtoPedido.getQuantidadeTotalProdutos() * produto.getPreco());
                }
            }
            quantidadeItens += 1;
        }
    }
}
