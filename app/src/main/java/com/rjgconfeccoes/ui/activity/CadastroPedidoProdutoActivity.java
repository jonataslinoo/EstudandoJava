package com.rjgconfeccoes.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.ui.adapters.AdapterProdutoPedidoProduto;
import com.rjgconfeccoes.ui.util.Util;

public class CadastroPedidoProdutoActivity extends AppCompatActivity {

    private TextView mensagem;
    private RecyclerView recyclerViewPedidoProdutos;
    private AdapterProdutoPedidoProduto adapterProdutos;
    private Button botaoGravar;
    private Button botaoVoltar;
    private Dados dados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedido_produto);

        inicializaCampos();
        configuraAdapterProdutos();
        configuraBotaoGravar();
        configuraBotaoVoltar();
    }

    private void inicializaCampos() {
        recyclerViewPedidoProdutos = findViewById(R.id.recyclerview_pedido_produto);
        botaoGravar = findViewById(R.id.botao_gravar_pedido_produto);
        botaoVoltar = findViewById(R.id.botao_voltar_pedido_produto);
        mensagem = findViewById(R.id.tv_nao_existe_pedido_produto);
    }

    private void configuraAdapterProdutos() {
        dados = Util.recuperaDados();

        if (dados.obtemListaProdutosPedido().size() == 0) {
            recyclerViewPedidoProdutos.setVisibility(View.GONE);
            mensagem.setVisibility(View.VISIBLE);
            mensagem.setText(getString(R.string.nao_existem_produtos));
        }

        if (dados.obtemListaProdutosSelecionados().size() == 0) {
            for (Produto produto : dados.obtemListaProdutosPedido()) {
                produto.setQuantidadeTotalProdutoPedido(0);
            }
        }

        adapterProdutos = new AdapterProdutoPedidoProduto(this, dados.obtemListaProdutosPedido());
        recyclerViewPedidoProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPedidoProdutos.setAdapter(adapterProdutos);
    }

    private void configuraBotaoGravar() {
        botaoGravar.setOnClickListener(view -> gravaItensEFecha());
    }

    private void configuraBotaoVoltar() {
        botaoVoltar.setOnClickListener(view -> limpaListaSelecionadosEFecha());
    }

    public void limpaListaSelecionadosEFecha() {
        adapterProdutos.notifyDataSetChanged();
        finish();
    }

    /**
     * Se o produto tiver quantidade maior que zero adiciona na lista de produtos selecioandos
     * senão remove da lista caso esteja lançado na mesma.
     */
    private void gravaItensEFecha() {
        for (Produto produto : dados.obtemListaProdutosPedido()) {
            if (produto.getQuantidadeTotalProdutoPedido() > 0) {
                if (!dados.obtemListaProdutosSelecionados().contains(produto)) {
                    dados.obtemListaProdutosSelecionados().add(produto);
                }
            } else {
                if (dados.obtemListaProdutosSelecionados().contains(produto)) {
                    dados.obtemListaProdutosSelecionados().remove(produto);
                }
            }
        }
        finish();
    }
}