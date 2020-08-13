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
import com.rjgconfeccoes.model.Pedidos;
import com.rjgconfeccoes.model.ProdutoPedido;
import com.rjgconfeccoes.ui.activity.VisualizarPedidoActivity;
import com.rjgconfeccoes.ui.adapters.AdapterPedidos;
import com.rjgconfeccoes.ui.adapters.OnItemClickListener;
import com.rjgconfeccoes.ui.util.Preferencias;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

import static com.rjgconfeccoes.ui.Const.Constantes.CHAVE_TESTE;

public class PedidosFragment extends Fragment {

    private static final int REMOVER_PEDIDO = 0;
    private static final int FINALIZAR_PEDIDO = 1;
    public static final int CANCELA_EVENTO = -1;
    private AdapterPedidos adapter;
    private ArrayList<Pedidos> listaPedidos;
    private RecyclerView recyclerViewPedidos;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerPedidos;
    private AlertDialog alertDialog;
    private TextView tvNaoExistePedido;
    private ArrayList<ProdutoPedido> listaProdutosPedido;
    private String idPedido;
    private String idCliente;
    private int posicaoClicada;
    private Pedidos pedidoClicado;

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
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        tvNaoExistePedido = view.findViewById(R.id.tv_nao_existe_pedido_pedido);

        //Abre o progresso de busca dos pedidos
        alertDialog = Util.criaProgressBar(getContext(), "Carregando Pedidos");
        alertDialog.show();

        //inicio minha lista
        listaPedidos = new ArrayList<>();

        //recupero os pedidos salvos no banco
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS);
        valueEventListenerPedidos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    //limpo minnha lista
                    listaPedidos.clear();

                    //listar pedidos
                    for (DataSnapshot idPedidoBanco : snapshot.getChildren()) {
                        String chaveGeral = idPedidoBanco.getKey();
                        Pedidos pedidos = new Pedidos();
                        separaStringIdentificador(idPedidoBanco.getKey());

                        listaProdutosPedido = new ArrayList<>();
                        for (DataSnapshot produtosPedidoBanco : snapshot.child(chaveGeral).getChildren()) {
                            ProdutoPedido produtoPedido = produtosPedidoBanco.getValue(ProdutoPedido.class);
                            listaProdutosPedido.add(produtoPedido);
                        }
                        pedidos.setId(idPedido);
                        pedidos.setClienteId(idCliente);
                        pedidos.setListaProdutosPedido(listaProdutosPedido);
                        listaPedidos.add(pedidos);
                    }

                    tvNaoExistePedido.setVisibility(View.GONE);
                    recyclerViewPedidos.setVisibility(View.VISIBLE);
                } else {
                    if (listaPedidos.size() == 0) {
                        tvNaoExistePedido.setVisibility(View.VISIBLE);
                        recyclerViewPedidos.setVisibility(View.GONE);
                    }
                }

                //Recupero os dados do sistema e atualizo a lista de pedidos e salvo nos dados
                Dados dados = Util.recuperaDados();
                dados.obtemListaPedidos().clear();
                dados.obtemListaPedidos().addAll(listaPedidos);
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
        adapter = new AdapterPedidos(getActivity(), listaPedidos);
        recyclerViewPedidos = view.findViewById(R.id.recyclerview_pedido_pedido);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPedidos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        registerForContextMenu(recyclerViewPedidos);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemLongClickListener(int position, Pedidos pedido) {
                posicaoClicada = position;
                pedidoClicado = pedido;
            }

            @Override
            public void onItemClickListener(int position, Pedidos pedido) {
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
        inflater.inflate(R.menu.menu_lista_pedidos, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Preferencias preferencias = new Preferencias(getActivity());
        String usuarioLogado = preferencias.getNomeUsuarioLogado();

        switch (item.getItemId()) {
            case R.id.menuRemover: {
                if (usuarioLogado.toLowerCase().equals(CHAVE_TESTE)) {
                    dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.conta_teste_nao_remove_dados), CANCELA_EVENTO);
                } else {
                    dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.corpo_msg_remover_pedido), REMOVER_PEDIDO);
                }
                break;
            }
            case R.id.menuFinalizar: {
                if (usuarioLogado.toLowerCase().equals(CHAVE_TESTE)) {
                    dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.conta_teste_nao_remove_dados), CANCELA_EVENTO);
                } else {
                    dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.corpo_msg_finalizar_pedido), FINALIZAR_PEDIDO);
                }
                break;
            }
        }
        return super.

                onContextItemSelected(item);

    }

    public void dialogMensagem(String titulo, String mensagem, int eventoClick) {
        new android.app.AlertDialog
                .Builder(getActivity())
                .setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton(R.string.botao_msg_confirmar, (dialogInterface, i) -> {
                    switch (eventoClick) {
                        case REMOVER_PEDIDO: {
                            removerPedido();
                            break;
                        }
                        case FINALIZAR_PEDIDO: {
                            finalizarPedido();
                            break;
                        }
                    }
                })
                .setNegativeButton(R.string.botao_msg_cancelar, null)
                .show();
    }

    /**
     * remove o pedido do banco de dados dos pedidos
     * e lança na lista de pedidos finalizados
     */
    private void finalizarPedido() {
        long id = Util.obtemDataAtualComHoraSegundo();
        String idPedido = pedidoClicado.getClienteId() + ";" + pedidoClicado.getId();
        String idPedidoFinalizado = pedidoClicado.getClienteId() + ";" + pedidoClicado.getId() + ";" + id;

        //Instancio uma referencia ao banco de dados
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS_FINALIZADOS).child(idPedidoFinalizado);

        for (ProdutoPedido produtoPedido : pedidoClicado.getListaProdutosPedido()) {
            databaseReference.child(produtoPedido.getProdutoId()).setValue(produtoPedido);
        }
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS).child(idPedido);
        databaseReference.removeValue();

        listaPedidos.remove(posicaoClicada);
    }

    /**
     * remove o pedido do banco de dados
     */
    public void removerPedido() {
        String idPedido = pedidoClicado.getClienteId() + ";" + pedidoClicado.getId();
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS).child(idPedido);
        databaseReference.removeValue();

        listaPedidos.remove(posicaoClicada);
    }

    private void chamaTelaVisualizarPedido() {
        Dados dados = Util.recuperaDados();
        dados.setVisualizarPedido(pedidoClicado);
        Intent intent = new Intent(getActivity(), VisualizarPedidoActivity.class);
        startActivity(intent);
    }

    private void separaStringIdentificador(String chaveIdentificacao) {
        String[] identificadores = chaveIdentificacao.split(";");
        idCliente = identificadores[0];
        idPedido = identificadores[1];
    }
}