package com.rjgconfeccoes.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rjgconfeccoes.R;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Pedido;
import com.rjgconfeccoes.model.ProdutoPedido;
import com.rjgconfeccoes.ui.activity.VisualizarPedidoActivity;
import com.rjgconfeccoes.ui.adapters.AdapterPedidos;
import com.rjgconfeccoes.ui.adapters.OnItemClickListener;
import com.rjgconfeccoes.ui.util.Preferencias;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

import static com.rjgconfeccoes.ui.constantes.Constantes.CHAVE_TESTE;


public class PedidosFinalizadosFragment extends Fragment {

    private static final int REMOVER_PEDIDO = 0;
    private static final int FINALIZAR_PEDIDO = 1;
    public static final int CANCELA_EVENTO = -1;
    private AdapterPedidos adapter;
    private ArrayList<Pedido> listaPedidoFinalizados = new ArrayList<>();
    private RecyclerView recyclerViewPedidosFinalizados;
    private TextView tvNaoExistePedidosFinalizados;
    private ArrayList<ProdutoPedido> listaProdutosPedido;
    private String idPedidoFinalizado;
    private String idCliente;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerPedidos;
    private AlertDialog alertDialog;
    private Pedido pedidoClicado;
    private int posicaoClicada;

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerPedidos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerPedidos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos_finalizados, container, false);
        tvNaoExistePedidosFinalizados = view.findViewById(R.id.tv_nao_existe_pedidosFinalizados);

        //Abre o progresso de busca dos pedidos
        alertDialog = Util.criaProgressBar(getContext(), "Carregando Pedidos Finzalizados");
        alertDialog.show();

        //recupero os pedidos finalizados salvos no banco
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS_FINALIZADOS);
        valueEventListenerPedidos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //limpo minha lista de pedidos finalizados
                listaPedidoFinalizados.clear();

                if (snapshot.getValue() != null) {
                    //listar pedidos finalizados
                    for (DataSnapshot idPedidoBanco : snapshot.getChildren()) {
                        String chaveGeral = idPedidoBanco.getKey();
                        Pedido pedido = new Pedido();
                        separaStringIdentificador(chaveGeral);

                        listaProdutosPedido = new ArrayList<>();
                        for (DataSnapshot produtosPedidoBanco : snapshot.child(chaveGeral).getChildren()) {
                            ProdutoPedido produtoPedido = produtosPedidoBanco.getValue(ProdutoPedido.class);
                            listaProdutosPedido.add(produtoPedido);
                        }
                        pedido.setId(idPedidoFinalizado);
                        pedido.setClienteId(idCliente);
                        pedido.setListaProdutosPedido(listaProdutosPedido);
                        listaPedidoFinalizados.add(pedido);
                    }

                    tvNaoExistePedidosFinalizados.setVisibility(View.GONE);
                    recyclerViewPedidosFinalizados.setVisibility(View.VISIBLE);
                } else {
                    tvNaoExistePedidosFinalizados.setVisibility(View.VISIBLE);
                    recyclerViewPedidosFinalizados.setVisibility(View.GONE);
                }

                //Recupero os dados do sistema e atualizo a lista de pedidos finalizados e salvo nos dados
                Dados dados = Util.recuperaDados();
                dados.obtemListaPedidos().clear();
                dados.obtemListaPedidos().addAll(listaPedidoFinalizados);
                Util.defineDados(dados);

                //Atualiza lista e finaliza o progresso de busca dos pedidos
                adapter.notifyDataSetChanged();
                Util.escondeProgressBar(alertDialog);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        configuraAdapter(view);

        return view;
    }

    private void configuraAdapter(View view) {
        adapter = new AdapterPedidos(getActivity(), listaPedidoFinalizados);
        recyclerViewPedidosFinalizados = view.findViewById(R.id.recyclerview_pedidosFinalizados);
        recyclerViewPedidosFinalizados.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPedidosFinalizados.setAdapter(adapter);
        registerForContextMenu(recyclerViewPedidosFinalizados);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemLongClickListener(int position, Pedido pedido) {
                pedidoClicado = pedido;
                posicaoClicada = position;
            }

            @Override
            public void onItemClickListener(int position, Pedido pedido) {
                posicaoClicada = position;
                pedidoClicado = pedido;

                chamaTelaVisualizarPedido();
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_remover, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Preferencias preferencias = new Preferencias(getActivity());
        String usuarioLogado = preferencias.getNomeUsuarioLogado();

        switch (item.getItemId()) {
            case R.id.menuRemove: {
                if (usuarioLogado.toLowerCase().equals(CHAVE_TESTE)) {
                    dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.conta_teste_nao_remove_dados), CANCELA_EVENTO);
                } else {
                    dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.corpo_msg_remover_pedido_finalizado), REMOVER_PEDIDO);
                }
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    public void dialogMensagem(String titulo, String mensagem, int eventoClick) {
        new android.app.AlertDialog
                .Builder(getActivity())
                .setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.botao_msg_confirmar), (dialogInterface, i) -> {
                    switch (eventoClick) {
                        case REMOVER_PEDIDO: {
                            removerPedido();
                            break;
                        }
                    }
                })
                .setNegativeButton(getString(R.string.botao_msg_cancelar), null)
                .show();
    }

    /**
     * remove o pedido do banco de dados
     */
    public void removerPedido() {
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS_FINALIZADOS).child(pedidoClicado.getId());
        databaseReference.removeValue();
    }

    private void separaStringIdentificador(String chaveIdentificacao) {
        String[] identificadores = chaveIdentificacao.split(";");
        idPedidoFinalizado = chaveIdentificacao;
        idCliente = identificadores[0];
    }

    private void chamaTelaVisualizarPedido() {
        Dados dados = Util.recuperaDados();
        dados.setVisualizarPedido(pedidoClicado);
        Intent intent = new Intent(getActivity(), VisualizarPedidoActivity.class);
        startActivity(intent);
    }
}