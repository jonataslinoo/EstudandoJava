package com.rjgconfeccoes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Cliente;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.ui.util.Base64Custom;

import java.util.List;

public class AdapterProdutosFragment extends RecyclerView.Adapter<AdapterProdutosFragment.ViewHolderProdutos> {

    private final Context context;
    private final List<Produto> listProdutos;

    public AdapterProdutosFragment(Context context, List<Produto> listProdutos) {
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
    }

    @Override
    public int getItemCount() {
        return listProdutos.size();
    }

    public class ViewHolderProdutos extends RecyclerView.ViewHolder {

        private final TextView idProduto;
        private final TextView nomePoduto;
        private final TextView quantidadeMasculina;
        private final TextView quantidadeFemina;
        private final TextView precoProduto;

        public ViewHolderProdutos(@NonNull View itemView) {
            super(itemView);

            idProduto = itemView.findViewById(R.id.tv_id_produto_fragment);
            nomePoduto = itemView.findViewById(R.id.tv_nome_produto_fragment);
            quantidadeMasculina = itemView.findViewById(R.id.tv_quantidade_masculina_produto_fragment);
            quantidadeFemina = itemView.findViewById(R.id.tv_quantidade_feminina_produto_fragment);
            precoProduto = itemView.findViewById(R.id.tv_preco_produto_fragment);
        }

        public void vincula(Produto produto) {
            String idProd = Base64Custom.codificarStringBase64(produto.getNome());
            int quantidadeTotal = 0;

            quantidadeTotal = produto.getQuantidadeMasculina() + produto.getQuantidadeFeminina();

            idProduto.setText(idProd);
            nomePoduto.setText(produto.getNome());

            if (quantidadeTotal == 1) {
                if (produto.getQuantidadeMasculina() == 0) {
                    quantidadeMasculina.setVisibility(View.GONE);
                    quantidadeFemina.setVisibility(View.VISIBLE);
                    quantidadeFemina.setText(produto.getQuantidadeFeminina());
                } else {
                    quantidadeFemina.setVisibility(View.GONE);
                    quantidadeMasculina.setVisibility(View.VISIBLE);
                    quantidadeMasculina.setText(produto.getQuantidadeMasculina());
                }
            } else {
                quantidadeFemina.setVisibility(View.VISIBLE);
                quantidadeMasculina.setVisibility(View.VISIBLE);
                quantidadeFemina.setText(produto.getQuantidadeMasculina());
                quantidadeMasculina.setText(produto.getQuantidadeMasculina());
            }
            precoProduto.setText("R$ " + produto.getPreco());
        }

        private void preencheCampos(Cliente cliente) {
            nomePoduto.setText(cliente.getNome());
            idProduto.setText(cliente.getTelefoneComArea());
        }
    }
}
