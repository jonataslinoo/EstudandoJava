package com.rjgconfeccoes.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Pedidos;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.model.ProdutoPedido;
import com.rjgconfeccoes.ui.adapters.AdapterVisualizarProdutosPedido;
import com.rjgconfeccoes.ui.util.Base64Custom;
import com.rjgconfeccoes.ui.util.Util;

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

    //variaveis da classe
    private Dados dados;
    private double valorTotal;
    private int quantidadeItens;
    private AdapterVisualizarProdutosPedido adapterProdutos;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pedido);

        dados = Util.recuperaDados();
        inicializaCampos();
        configuraToolbar();
        carregaDados();
    }

    private void inicializaCampos() {
        toolbar = findViewById(R.id.toolbar);
        tvDescPedido = findViewById(R.id.tv_desc_pedido_visualizarPedido);
        tvPedidoFinalizado = findViewById(R.id.tv_pedido_finalizado_visualizarPedido);
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

    private void carregaDados() {
        Pedidos pedido = dados.getVisualizarPedido();
        retornaValorPedido(pedido);
        String[] identificadores = pedido.getId().split(";");

        tvPrecoPedido.setText("Valor total: R$ " + Util.formataPreco(valorTotal));

        if (quantidadeItens > 1) {
            tvQuantidadeItens.setText("Quantidade: " + quantidadeItens + " Itens");
        } else {
            tvQuantidadeItens.setText("Quantidade: " + quantidadeItens + " Item");
        }

        //se for maior que 2 é pedido finalizado
        if (identificadores.length > 2) {
            tvPedidoFinalizado.setVisibility(View.VISIBLE);
            layoutDataCriadaFinalizada.setVisibility(View.VISIBLE);
            tvDescPedido.setText("Pedido: " + identificadores[1]);
            tvPedidoFinalizado.setText("Finalizado");
            tvNomeCliente.setText("Cliente: " + Base64Custom.decodificarStringBase64(identificadores[0]));
            tvDataCriada.setText(Util.converteDataHorasSegundos(Long.parseLong(identificadores[1])));
            tvDataFinalizada.setText(Util.converteDataHorasSegundos(Long.parseLong(identificadores[2])));
        } else {
            layoutData.setVisibility(View.VISIBLE);
            tvDescPedido.setText("Pedido: " + pedido.getId());
            tvNomeCliente.setText("Cliente: " + Base64Custom.decodificarStringBase64(pedido.getClienteId()));
            tvData.setText(Util.converteDataHorasSegundos(Long.parseLong(pedido.getId())));
        }

        adapterProdutos = new AdapterVisualizarProdutosPedido(this, pedido.getListaProdutosPedido());
        recyclerViewProdutosVisualizar.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProdutosVisualizar.setAdapter(adapterProdutos);
    }

    private void retornaValorPedido(Pedidos pedido) {
        Dados dados = Util.recuperaDados();

        valorTotal = 0.0;
        quantidadeItens = 0;

        for (ProdutoPedido produtoPedido : pedido.getListaProdutosPedido()) {
            for (Produto produto : dados.obtemListaProdutos()) {
                if (produtoPedido.getProdutoId().equals(produto.getDescricao())) {
                    valorTotal += (produtoPedido.getQuantidadeTotalProdutos() * produto.getPreco());
                }
            }
            quantidadeItens += 1;
        }
    }

}
