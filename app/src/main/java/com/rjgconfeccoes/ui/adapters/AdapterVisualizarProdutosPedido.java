package com.rjgconfeccoes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.model.ProdutoPedido;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

public class AdapterVisualizarProdutosPedido extends RecyclerView.Adapter<AdapterVisualizarProdutosPedido.ViewHolder> {

    private final Context context;
    private final ArrayList<ProdutoPedido> listaProdutosPedido;

    public AdapterVisualizarProdutosPedido(Context context, ArrayList<ProdutoPedido> listaProdutosPedido) {
        this.context = context;
        this.listaProdutosPedido = listaProdutosPedido;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.lst_visualizar_produtos_pedido, parent, false);
        return new AdapterVisualizarProdutosPedido.ViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProdutoPedido produtoPedido = listaProdutosPedido.get(position);
        Dados dados = Util.recuperaDados();

        for (Produto produto : dados.obtemListaProdutos()) {
            if (produtoPedido.getProdutoId().equals(produto.getDescricao())) {
                holder.vincula(produto, produtoPedido);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaProdutosPedido.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNomeProduto;
        private final TextView tvPrimeiroItem;
        private final TextView tvSegundoItem;
        private final TextView tvPreco;
        private final TextView tvQuantidade;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNomeProduto = itemView.findViewById(R.id.tv_nome_produto_visualizarPedido);
            tvPrimeiroItem = itemView.findViewById(R.id.tv_quantidade_masculina_produto_visualizarPedido);
            tvSegundoItem = itemView.findViewById(R.id.tv_quantidade_feminina_produto_visualizarPedido);
            tvPreco = itemView.findViewById(R.id.tv_preco_produto_visualizarPedido);
            tvQuantidade = itemView.findViewById(R.id.tv_quantidade_produto_visualizarPedido);
        }

        public void vincula(Produto produto, ProdutoPedido produtoPedido) {
            tvNomeProduto.setText(produto.getDescricao());

            if (produto.getQuantidadeMasculina() == 0) {
                tvPrimeiroItem.setText("Feminina: " + produto.getQuantidadeFeminina());
                tvSegundoItem.setVisibility(View.INVISIBLE);
            } else if (produto.getQuantidadeFeminina() == 0) {
                tvPrimeiroItem.setText("Masculina: " + produto.getQuantidadeMasculina());
                tvSegundoItem.setVisibility(View.INVISIBLE);
            } else {
                tvPrimeiroItem.setText("Masculina: " + produto.getQuantidadeMasculina());
                tvSegundoItem.setText("Feminina: " + produto.getQuantidadeFeminina());
            }

            tvPreco.setText("R$ " + Util.formataPreco(produto.getPreco()));
            tvQuantidade.setText("Quantidade: " + produtoPedido.getQuantidadeTotalProdutos());
        }
    }
}
