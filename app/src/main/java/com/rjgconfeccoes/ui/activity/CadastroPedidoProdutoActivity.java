package com.rjgconfeccoes.ui.activity;

import android.content.Intent;
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
import com.rjgconfeccoes.ui.adapters.AdapterProdutos;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

public class CadastroPedidoProdutoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private TextView mensagem;
    private RecyclerView recyclerViewPedidoProdutos;
    private AdapterProdutos adapterProdutos;
    private Button botaoGravar;
    private Button botaoVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedido_produto);

        inicializaCampos();
        configuraAdapterProdutos();
        configuraBotaoGravar();
    }

    private void inicializaCampos() {
        toolbar = findViewById(R.id.toolbar);
        constraintLayout = findViewById(R.id.constraint_layout_cadastrar_produto);
        recyclerViewPedidoProdutos = findViewById(R.id.recyclerview_pedido_produto);
        botaoGravar = findViewById(R.id.botao_gravar_pedido_produto);
        botaoVoltar = findViewById(R.id.botao_voltar_pedido_produto);
        mensagem = findViewById(R.id.tv_nao_existe_pedido_produto);
    }

    private void configuraAdapterProdutos() {
        Dados dados = Util.recuperaDados();

        if (dados.obtemListaProdutos().size() == 0) {
            recyclerViewPedidoProdutos.setVisibility(View.GONE);
            mensagem.setVisibility(View.VISIBLE);
            mensagem.setText(getString(R.string.nao_existem_produtos));
        }

        adapterProdutos = new AdapterProdutos(this, dados.obtemListaProdutos());
        recyclerViewPedidoProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPedidoProdutos.setAdapter(adapterProdutos);
    }

    private void configuraBotaoGravar(){
        botaoGravar.setOnClickListener(view -> finish());
    }

    private void vaiParaTelaPedido() {
        Intent intent = new Intent(CadastroPedidoProdutoActivity.this, CadastroPedidoActivity.class);
        startActivity(intent);
        finish();
    }
}