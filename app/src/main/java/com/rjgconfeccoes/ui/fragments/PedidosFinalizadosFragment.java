package com.rjgconfeccoes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.rjgconfeccoes.ui.adapters.AdapterPedidos;
import com.rjgconfeccoes.ui.adapters.OnItemClickListener;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;


public class PedidosFinalizadosFragment extends Fragment {

    private AdapterPedidos adapter;
    private ArrayList<Pedidos> listaPedidosFinalizados = new ArrayList<>();
    private RecyclerView recyclerViewPedidosFinalizados;
    private TextView tvNaoExistePedidosFinalizados;
    private ArrayList<ProdutoPedido> listaProdutosPedido;
    private String idPedidoFinalizado;
    private String idCliente;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerPedidos;
    private AlertDialog alertDialog;

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

        //recupero os clientes salvos no banco
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS_FINALIZADOS);
        valueEventListenerPedidos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //limpo minha lista de clientes
                listaPedidosFinalizados.clear();

                if (snapshot.getValue() != null) {
                    //listar clientes
                    for (DataSnapshot idPedidoBanco : snapshot.getChildren()) {
                        String chaveGeral = idPedidoBanco.getKey();
                        Pedidos pedidos = new Pedidos();
                        separaStringIdentificador(idPedidoBanco.getKey());

                        listaProdutosPedido = new ArrayList<>();
                        for (DataSnapshot produtosPedidoBanco : snapshot.child(chaveGeral).getChildren()) {
                            ProdutoPedido produtoPedido = produtosPedidoBanco.getValue(ProdutoPedido.class);
                            listaProdutosPedido.add(produtoPedido);
                        }
                        pedidos.setId(idPedidoFinalizado);
                        pedidos.setClienteId(idCliente);
                        pedidos.setListaProdutosPedido(listaProdutosPedido);
                        listaPedidosFinalizados.add(pedidos);
                    }

                    tvNaoExistePedidosFinalizados.setVisibility(View.GONE);
                    recyclerViewPedidosFinalizados.setVisibility(View.VISIBLE);
                } else {
                    tvNaoExistePedidosFinalizados.setVisibility(View.VISIBLE);
                    recyclerViewPedidosFinalizados.setVisibility(View.GONE);
                }

                //Recupero os dados do sistema e atualizo a lista de clientes e salvo nos dados
                Dados dados = Util.recuperaDados();
                dados.obtemListaPedidos().clear();
                dados.obtemListaPedidos().addAll(listaPedidosFinalizados);
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
        adapter = new AdapterPedidos(getActivity(), listaPedidosFinalizados);
        recyclerViewPedidosFinalizados = view.findViewById(R.id.recyclerview_pedidosFinalizados);
        recyclerViewPedidosFinalizados.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPedidosFinalizados.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemLongClickListener(int position, Pedidos pedido) {
            }

            @Override
            public void onItemClickListener(int position, Pedidos pedido) {
            }
        });
    }

    private void separaStringIdentificador(String chaveIdentificacao) {
        String[] identificadores = chaveIdentificacao.split(";");
        idPedidoFinalizado = chaveIdentificacao;
        idCliente = identificadores[0];
    }
}