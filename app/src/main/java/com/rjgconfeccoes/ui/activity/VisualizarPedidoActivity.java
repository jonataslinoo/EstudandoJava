package com.rjgconfeccoes.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;

public class VisualizarPedidoActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Visualizar Pedido";
    private Toolbar toolbar;
    private TextView tvDescPedido;
    private TextView tvPedidoFinalizado;
    private TextView tvNomeCliente;
    private TextView tvPrecoPedido;
    private TextView tvQuantidadeItens;
    private TextView tvData;
    private TextView tvDataCriada;
    private TextView tvDataFinalizada;
    private RecyclerView recyclerViewProdutosVisualizar;
    private LinearLayout layoutData;
    private LinearLayout layoutDataCriadaFinalizada;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pedido);

        inicializaCampos();
        configuraToolbar();

    }

    private void inicializaCampos() {
        toolbar = findViewById(R.id.toolbar);
        tvDescPedido = findViewById(R.id.tv_desc_pedido_visualizarPedido);
        tvPedidoFinalizado = findViewById(R.id.tv_pedido_finalizado_pedidoFragment);
        tvNomeCliente = findViewById(R.id.tv_nome_cliente_visualizarPedido);
        tvPrecoPedido = findViewById(R.id.tv_preco_total_pedido_visualizarPedido);
        tvQuantidadeItens = findViewById(R.id.tv_quantidade_itens_pedido_visualizarPedido);
        tvData = findViewById(R.id.tv_data_pedido_visualizarPedido);
        tvDataCriada = findViewById(R.id.tv_data_pedido_criado_visualizarPedido);
        tvDataFinalizada = findViewById(R.id.tv_data_pedido_finalizado_visualizarPedido);
        recyclerViewProdutosVisualizar = findViewById(R.id.recyclerview_visualizarPedido);
        layoutData = findViewById(R.id.lnl_data_pedido_visualizarPedido);
        layoutDataCriadaFinalizada = findViewById(R.id.lnl_data_criada_finalizada_visualizarPedido);
    }

    private void configuraToolbar() {
        setTitle(TITULO_APPBAR);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_voltar);
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}
