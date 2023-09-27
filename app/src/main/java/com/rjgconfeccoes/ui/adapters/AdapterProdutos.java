package com.rjgconfeccoes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.ui.util.Util;

import java.util.Collections;
import java.util.List;

public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.ViewHolderProdutos> {

    private final Context context;
    private final List<Produto> listProdutos;

    public AdapterProdutos(Context context, List<Produto> listProdutos) {
        this.context = context;
        this.listProdutos = listProdutos;
    }

    @NonNull
    @Override
    public ViewHolderProdutos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.lst_produtos_fragment, parent, false);
        return new ViewHolderProdutos(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProdutos holder, int position) {
        Collections.sort(listProdutos, (produto, produto2) -> produto.getDescricao().compareTo(produto2.getDescricao()));
        Produto produto = listProdutos.get(position);
        holder.vincula(produto);
    }

    @Override
    public int getItemCount() {
        return listProdutos.size();
    }

    public class ViewHolderProdutos extends RecyclerView.ViewHolder {

        private final TextView nomePoduto;
        private final TextView tvPrimeiraQuantidade;
        private final TextView tvSegundaQuantidade;
        private final TextView precoProduto;

        public ViewHolderProdutos(@NonNull View itemView) {
            super(itemView);

            nomePoduto = itemView.findViewById(R.id.tv_nome_produto_fragment);
            tvPrimeiraQuantidade = itemView.findViewById(R.id.tv_quantidade_masculina_produto_fragment);
            tvSegundaQuantidade = itemView.findViewById(R.id.tv_quantidade_feminina_produto_fragment);
            precoProduto = itemView.findViewById(R.id.tv_preco_produto_fragment);
        }

        public void vincula(Produto produto) {
            nomePoduto.setText(produto.getDescricao());

            if (produto.getQuantidadeMasculina() == 0) {
                tvPrimeiraQuantidade.setText("Feminina: " + produto.getQuantidadeFeminina());
                tvSegundaQuantidade.setVisibility(View.INVISIBLE);
            } else if (produto.getQuantidadeFeminina() == 0) {
                tvPrimeiraQuantidade.setText("Masculina: " + produto.getQuantidadeMasculina());
                tvSegundaQuantidade.setVisibility(View.INVISIBLE);
            } else {
                tvPrimeiraQuantidade.setText("Masculina: " + produto.getQuantidadeMasculina());
                tvSegundaQuantidade.setText("Feminina: " + produto.getQuantidadeFeminina());
            }

            precoProduto.setText("R$ " + Util.formataPreco(produto.getPreco()));
        }
    }
}
