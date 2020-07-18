package com.rjgconfeccoes.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;

public class CadastroPedidoActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "Novo Pedido";
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private Spinner spinnerCliente;
    private RecyclerView recyclerViewProdutos;
    private Button botaoGravarPedido;
    private Button botaoListaProdutos;
    private CheckBox kitAdulto;
    private CheckBox kitInfantil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedido);

        inicializaCampos();
        configuraToolbar();
//        configuraAdicionarRemoverQuantidade();
//        configuraBotaoCadastrar();
//        configuraKits();
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
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}