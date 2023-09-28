package com.rjgconfeccoes.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.rjgconfeccoes.R;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.model.Cliente;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Pedido;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.model.ProdutoPedido;
import com.rjgconfeccoes.ui.adapters.AdapterProdutoPedidoProduto;
import com.rjgconfeccoes.ui.util.Base64Custom;
import com.rjgconfeccoes.ui.util.Preferencias;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

import static com.rjgconfeccoes.ui.Const.Constantes.CHAVE_TESTE;

public class CadastroPedidoActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "Novo Pedido";
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private Spinner spinnerCliente;
    private RecyclerView recyclerViewProdutos;
    private AdapterProdutoPedidoProduto adapterProdutoPedidoProduto;
    private Button botaoGravarPedido;
    private Button botaoListaProdutos;
    private TextView valorTotalPedido;
    private DatabaseReference databaseReference;
    Dados dados = Util.recuperaDados();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pedido);

        inicializaCampos();
        configuraToolbar();
        preencheListaClientes();
        configuraBotoes();
        configuraAdapterProdutos();
        atualizaValorTotal();
    }

    private void inicializaCampos() {
        toolbar = findViewById(R.id.toolbar);
        constraintLayout = findViewById(R.id.constraint_layout_cadastrar_pedido);
        spinnerCliente = findViewById(R.id.spinner_cliente_cadastrar_pedido);
        recyclerViewProdutos = findViewById(R.id.recyclerView_produtos_cadastrar_pedido);
        botaoGravarPedido = findViewById(R.id.bt_gravar_cadastrar_pedido);
        botaoListaProdutos = findViewById(R.id.bt_produtos_cadastrar_pedido);
        valorTotalPedido = findViewById(R.id.tv_valor_total_pedido);
    }

    private void configuraToolbar() {
        setTitle(TITULO_APPBAR);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_voltar);
        toolbar.setNavigationOnClickListener(view -> vaiParaTelaDashborad());
    }

    private void configuraAdapterProdutos() {
        if (dados.obtemListaProdutosSelecionados() != null) {
            adapterProdutoPedidoProduto = new AdapterProdutoPedidoProduto(this, dados.obtemListaProdutosSelecionados());
            recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewProdutos.setAdapter(adapterProdutoPedidoProduto);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        vaiParaTelaDashborad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaValorTotal();
        adapterProdutoPedidoProduto.notifyDataSetChanged();
    }

    private void atualizaValorTotal() {
        double valorTotal = 0.0;

        if (dados.obtemListaProdutosSelecionados() != null) {
        }
        for (Produto produto : dados.obtemListaProdutosSelecionados()) {
            valorTotal += (produto.getPreco() * produto.getQuantidadeTotalProdutoPedido());
        }
        valorTotalPedido.setText("R$ " + Util.formataPreco(valorTotal));
    }

    private void preencheListaClientes() {
        ArrayList<Cliente> listaCliente = new ArrayList<>(dados.obtemListaClientes());

        //valida para lançar um texto  de selecione um cliente
        if (listaCliente.size() > 0) {
            Cliente cliente;
            cliente = listaCliente.get(0);
            if (!cliente.getNome().equals(Util.SELECIONE_CLIENTE)) {
                cliente = new Cliente();
                cliente.setId("");
                cliente.setNome(Util.SELECIONE_CLIENTE);
                cliente.setTelefoneComArea("");

                listaCliente.add(0, cliente);
            }
        } else {
            dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.nao_possui_clientes_carregue_lista));
        }

        //Cria o adapter
        ArrayAdapter<Cliente> dadosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCliente);
        dadosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(dadosAdapter);
    }

    private void configuraBotoes() {
        botaoListaProdutos.setOnClickListener(view -> abreListaProdutos());
        botaoGravarPedido.setOnClickListener(view -> validaCamposDigitados());
    }

    private void validaCamposDigitados() {
        Preferencias preferencias = new Preferencias(this);
        String usuarioLogado = preferencias.getNomeUsuarioLogado();
        if (usuarioLogado.toLowerCase().equals(CHAVE_TESTE)) {
            dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.conta_teste_nao_grava_dados));
            limpaCampos();
        } else {
            String clienteSelecionado = spinnerCliente.getSelectedItem().toString();
            if (clienteSelecionado.isEmpty() || clienteSelecionado.equals(Util.SELECIONE_CLIENTE)) {
                Util.mensagemDeAlerta(CadastroPedidoActivity.this, constraintLayout, getString(R.string.msg_erro_selecione_cliente));
            } else if (dados.obtemListaProdutosSelecionados().size() <= 0) {
                Util.mensagemDeAlerta(CadastroPedidoActivity.this, constraintLayout, getString(R.string.msg_erro_pedido_sem_produto));
            } else {
                salvaDadosPedido(clienteSelecionado);
            }
        }
    }

    private void salvaDadosPedido(String clienteSelecionado) {
        Pedido pedido = new Pedido();
        String idClienteCodificado = Base64Custom.codificarStringBase64(clienteSelecionado);
        pedido.setClienteId(idClienteCodificado);
        cadastrarPedido(pedido);
    }

    private void cadastrarPedido(Pedido pedido) {
        String idPedido = retornaIdentifcadorPedido(pedido);

        //Instancio uma referencia ao banco de dados
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS).child(idPedido);

        ProdutoPedido produtoPedido = new ProdutoPedido();
        for (Produto produto : dados.obtemListaProdutosSelecionados()) {
            produtoPedido.setProdutoId(produto.getDescricao());
            produtoPedido.setQuantidadeTotalProdutos(produto.getQuantidadeTotalProdutoPedido());

            String idProduto = Base64Custom.codificarStringBase64(produto.getDescricao());

            databaseReference.child(idProduto).setValue(produtoPedido);
        }

        Util.mensagemDeAlerta(CadastroPedidoActivity.this, constraintLayout, getString(R.string.msg_sucesso_cadastrar_pedido));
        vaiParaTelaDashborad();
    }

    private String retornaIdentifcadorPedido(Pedido pedido) {
        //uso a data como identificador unico aproveito para mostrar na tela a data do pedido
        long dataPedidoIdentificador = Util.obtemDataAtualComHoraSegundo();
        String identificador = pedido.getClienteId() + ";" + dataPedidoIdentificador;

        return identificador;
    }

    private void abreListaProdutos() {
        Intent intent = new Intent(CadastroPedidoActivity.this, CadastroPedidoProdutoActivity.class);
        startActivity(intent);
    }

    private void limpaCampos() {
        spinnerCliente.setSelection(0);
        valorTotalPedido.setText("R$ " + Util.formataPreco(0.0));
        dados.obtemListaProdutosSelecionados().clear();
        adapterProdutoPedidoProduto.notifyDataSetChanged();
    }

    private void vaiParaTelaDashborad() {
        limpaCampos();
        finish();
    }

    public void dialogMensagem(String titulo, String mensagem) {
        new AlertDialog
                .Builder(this)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.botao_msg_ok), null)
                .show();
    }
}