package com.rjgconfeccoes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.ui.activity.CadastroPedidoActivity;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

public class AdapterProdutoPedidoProduto extends RecyclerView.Adapter<AdapterProdutoPedidoProduto.ViewHolderProdutoPedidoProduto> {

    private final Context context;
    private final ArrayList<Produto> listaProdutos;
    private Dados dados = Util.recuperaDados();

    public AdapterProdutoPedidoProduto(Context context, ArrayList<Produto> listaProdutos) {
        this.context = context;
        this.listaProdutos = listaProdutos;
    }

    @NonNull
    @Override
    public ViewHolderProdutoPedidoProduto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.lst_produtos_pedido_produto, parent, false);
        return new ViewHolderProdutoPedidoProduto(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProdutoPedidoProduto holder, int position) {
        Produto produto = listaProdutos.get(position);
        holder.vincula(produto);
        holder.botaoAdicionar.setOnClickListener(view -> adicionaOuRemoveQuantidade(view, produto, holder));
        holder.botaoRemover.setOnClickListener(view -> adicionaOuRemoveQuantidade(view, produto, holder));
        holder.checkBoxAdicionarProduto.setOnClickListener(view -> adicionaOuRemoveQuantidade(view, produto, holder));
    }

    private void adicionaOuRemoveQuantidade(View view, Produto produto, ViewHolderProdutoPedidoProduto holder) {
        int quantidadeProduto = 0;

        quantidadeProduto = produto.getQuantidadeTotalProdutoPedido();

        if (view == holder.botaoAdicionar) {
            if (produto.getQuantidadeTotalProdutoPedido() == 0) {
                quantidadeProduto++;
                produto.setQuantidadeTotalProdutoPedido(quantidadeProduto);
                dados.obtemListaProdutosSelecionados().add(produto);
                holder.checkBoxAdicionarProduto.setChecked(true);
            } else {
                quantidadeProduto++;
                produto.setQuantidadeTotalProdutoPedido(quantidadeProduto);
            }

        } else if (view == holder.botaoRemover) {
            if (produto.getQuantidadeTotalProdutoPedido() > 0) {
                if (produto.getQuantidadeTotalProdutoPedido() == 1) {
                    quantidadeProduto--;
                    produto.setQuantidadeTotalProdutoPedido(quantidadeProduto);
                    dados.obtemListaProdutosSelecionados().remove(produto);
                    holder.checkBoxAdicionarProduto.setChecked(false);
                } else {
                    quantidadeProduto--;
                    produto.setQuantidadeTotalProdutoPedido(quantidadeProduto);
                }
            }
        } else if (view == holder.checkBoxAdicionarProduto) {
            if (dados.obtemListaProdutosSelecionados().contains(produto)) {
                quantidadeProduto = 0;
                produto.setQuantidadeTotalProdutoPedido(quantidadeProduto);
                dados.obtemListaProdutosSelecionados().remove(produto);
            } else {
                quantidadeProduto = 1;
                produto.setQuantidadeTotalProdutoPedido(quantidadeProduto);
                dados.obtemListaProdutosSelecionados().add(produto);
            }
        }

        holder.quantidadeProduto.setText("Quantidade: " + quantidadeProduto);
        holder.precoTotal.setText("Total R$ " + Util.formataPreco((produto.getPreco() * produto.getQuantidadeTotalProdutoPedido())));
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public class ViewHolderProdutoPedidoProduto extends RecyclerView.ViewHolder {

        public final LinearLayout itemPedido;
        public final TextView descricaoProduto;
        public final TextView quantidadeMasculina;
        public final TextView quantidadeFeminina;
        public final TextView quantidadeProduto;
        public final TextView precoProduto;
        public final TextView precoTotal;
        public final ImageView botaoAdicionar;
        public final ImageView botaoRemover;
        public final CheckBox checkBoxAdicionarProduto;
        public final LinearLayout layoutBotoesProdutoPedido;

        public ViewHolderProdutoPedidoProduto(@NonNull View itemView) {
            super(itemView);

            itemPedido = itemView.findViewById(R.id.lnl_item_pedidoProduto);
            descricaoProduto = itemView.findViewById(R.id.tv_descricao_produto_pedidoProduto);
            quantidadeMasculina = itemView.findViewById(R.id.tv_quantidade_masculina_pedidoProduto);
            quantidadeFeminina = itemView.findViewById(R.id.tv_quantidade_feminina_pedidoProduto);
            quantidadeProduto = itemView.findViewById(R.id.tv_quantidade_produto_pedidoProduto);
            precoProduto = itemView.findViewById(R.id.tv_preco_produto_pedidoProduto);
            precoTotal = itemView.findViewById(R.id.tv_preco_total_produto_pedidoProduto);
            botaoAdicionar = itemView.findViewById(R.id.botao_adicionar_quantidade_total_pedidoProduto);
            botaoRemover = itemView.findViewById(R.id.botao_remover_quantidade_total_pedidoProduto);
            checkBoxAdicionarProduto = itemView.findViewById(R.id.checkbox_adicionar_produto_pedidoProduto);
            layoutBotoesProdutoPedido = itemView.findViewById(R.id.lnl_botoes_pedidoProduto);
        }

        public void vincula(Produto produto) {
            descricaoProduto.setText(produto.getDescricao());
            quantidadeMasculina.setText("Masculina: " + produto.getQuantidadeMasculina());
            quantidadeFeminina.setText("Feminina: " + produto.getQuantidadeFeminina());
            precoProduto.setText("R$ " + Util.formataPreco(produto.getPreco()));
            quantidadeProduto.setText("Quantidade: " + produto.getQuantidadeTotalProdutoPedido());
            precoTotal.setText("Total R$ " + Util.formataPreco((produto.getPreco() * produto.getQuantidadeTotalProdutoPedido())));

            if (context instanceof CadastroPedidoActivity) {
                checkBoxAdicionarProduto.setVisibility(View.GONE);
                layoutBotoesProdutoPedido.setVisibility(View.GONE);
            } else {
                checkBoxAdicionarProduto.setVisibility(View.VISIBLE);
                layoutBotoesProdutoPedido.setVisibility(View.VISIBLE);
            }

            if (dados.obtemListaProdutosSelecionados().contains(produto)) {
                checkBoxAdicionarProduto.setChecked(true);
            }
        }
    }
}

