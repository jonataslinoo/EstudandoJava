package com.rjgconfeccoes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.ui.activity.CadastroPedidoProdutoActivity;
import com.rjgconfeccoes.ui.util.Util;

import java.util.List;

public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.ViewHolderProdutos> {

    private final Context context;
    private final List<Produto> listProdutos;
    private Dados dados = Util.recuperaDados();

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
        Produto produto = listProdutos.get(position);
        holder.vincula(produto);
        holder.checkBoxAdicionarProduto.setOnClickListener(view -> enviaProdutos(produto));
    }

    @Override
    public int getItemCount() {
        return listProdutos.size();
    }

    public void enviaProdutos(Produto produto) {
        if (dados.obtemListaProdutosSelecionados().contains(produto)) {
            dados.obtemListaProdutosSelecionados().remove(produto);
        } else {
            dados.obtemListaProdutosSelecionados().add(produto);
        }
    }

    public class ViewHolderProdutos extends RecyclerView.ViewHolder {

        private final TextView nomePoduto;
        private final TextView quantidadeMasculina;
        private final TextView quantidadeFemina;
        private final TextView precoProduto;
        private final CheckBox checkBoxAdicionarProduto;

        public ViewHolderProdutos(@NonNull View itemView) {
            super(itemView);

            nomePoduto = itemView.findViewById(R.id.tv_nome_produto_fragment);
            quantidadeMasculina = itemView.findViewById(R.id.tv_quantidade_masculina_produto_fragment);
            quantidadeFemina = itemView.findViewById(R.id.tv_quantidade_feminina_produto_fragment);
            precoProduto = itemView.findViewById(R.id.tv_preco_produto_fragment);
            checkBoxAdicionarProduto = itemView.findViewById(R.id.checkbox_adicionar_pedido_produto);
        }

        public void vincula(Produto produto) {
            if (context instanceof CadastroPedidoProdutoActivity) {
                checkBoxAdicionarProduto.setVisibility(View.VISIBLE);
            } else {
                checkBoxAdicionarProduto.setVisibility(View.GONE);
                checkBoxAdicionarProduto.setChecked(false);
            }

            if(dados.obtemListaProdutosSelecionados().contains(produto)){
                checkBoxAdicionarProduto.setChecked(true);
            }

            nomePoduto.setText(produto.getNome());
            quantidadeMasculina.setText("Masculina: " + produto.getQuantidadeMasculina());
            quantidadeFemina.setText("Feminina: " + produto.getQuantidadeFeminina());
            precoProduto.setText("R$ " + produto.getPreco());
        }

    }
}
