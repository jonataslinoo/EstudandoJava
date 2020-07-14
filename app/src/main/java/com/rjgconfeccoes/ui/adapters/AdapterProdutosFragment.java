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

        public final TextView idProduto;
        public final TextView nomePoduto;
        public final TextView tipoSexoProduto;
        public final TextView precoProduto;

        public ViewHolderProdutos(@NonNull View itemView) {
            super(itemView);

            idProduto = itemView.findViewById(R.id.tv_id_produto_fragment);
            nomePoduto = itemView.findViewById(R.id.tv_nome_produto_fragment);
            tipoSexoProduto = itemView.findViewById(R.id.tv_tipo_produto_fragment);
            precoProduto = itemView.findViewById(R.id.tv_preco_produto_fragment);
        }

        public void vincula(Produto produto) {
            String idProd = Base64Custom.codificarStringBase64(produto.getNome());

            idProduto.setText(idProd);
            nomePoduto.setText(produto.getNome());
            tipoSexoProduto.setText(produto.getTipoSexo().toUpperCase());
            precoProduto.setText("R$ " + produto.getPreco());
        }

        private void preencheCampos(Cliente cliente) {
            nomePoduto.setText(cliente.getNome());
            idProduto.setText(cliente.getTelefoneComArea());
        }
    }
}
