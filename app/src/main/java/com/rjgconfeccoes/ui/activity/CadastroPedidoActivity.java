package com.rjgconfeccoes.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Cliente;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.ui.adapters.AdapterProdutos;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

public class CadastroPedidoActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "Novo Pedido";
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private Spinner spinnerCliente;
    private RecyclerView recyclerViewProdutos;
    private AdapterProdutos adapterProdutos;
    private Button botaoGravarPedido;
    private Button botaoListaProdutos;
    private CheckBox kitAdulto;
    private CheckBox kitInfantil;
    Dados dados = Util.recuperaDados();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedido);

        inicializaCampos();
        configuraToolbar();
        preencheListaClientes();
        configuraBotaoProdutos();
//        configuraAdicionarRemoverQuantidade();
//        configuraBotaoCadastrar();
//        configuraKits();

        if(dados.obtemListaProdutosSelecionados() != null){
            adapterProdutos = new AdapterProdutos(this, dados.obtemListaProdutosSelecionados());
            recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewProdutos.setAdapter(adapterProdutos);
        }
    }

    private void inicializaCampos() {
        toolbar = findViewById(R.id.toolbar);
        constraintLayout = findViewById(R.id.constraint_layout_cadastrar_pedido);
        spinnerCliente = findViewById(R.id.spinner_cliente_cadastrar_pedido);
        recyclerViewProdutos = findViewById(R.id.recyclerView_produtos_cadastrar_pedido);
        botaoGravarPedido = findViewById(R.id.bt_gravar_cadastrar_pedido);
        botaoListaProdutos = findViewById(R.id.bt_produtos_cadastrar_pedido);
    }

    private void configuraToolbar() {
        setTitle(TITULO_APPBAR);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_voltar);
        toolbar.setNavigationOnClickListener(view -> limpaDadosEFinalizaTela());
    }

    private void limpaDadosEFinalizaTela(){
        dados.obtemListaProdutosSelecionados().clear();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        limpaDadosEFinalizaTela();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterProdutos.notifyDataSetChanged();
    }

    private void preencheListaClientes() {
        Dados dados = Util.recuperaDados();

        //Cria o adapter
        ArrayAdapter<Cliente> dadosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dados.obtemListaClientes());

        //Seta o adapter no dropdown
        dadosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Atacha o adapter ao controle
        spinnerCliente.setAdapter(dadosAdapter);
    }

    private void configuraBotaoProdutos() {
        botaoListaProdutos.setOnClickListener(view -> abreListaProdutos());
    }

    private void abreListaProdutos() {
        Intent intent = new Intent(CadastroPedidoActivity.this, CadastroPedidoProdutoActivity.class);
        startActivity(intent);
    }
}