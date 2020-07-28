package com.rjgconfeccoes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.rjgconfeccoes.ui.adapters.AdapterPedidos;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

public class PedidosFragment extends Fragment {

    private AdapterPedidos adapter;
    private ArrayList<Pedidos> listaPedidos = new ArrayList<>();
    private RecyclerView recyclerViewPedidos;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerPedidos;
    private AlertDialog alertDialog;
    private TextView tvNaoExistePedido;

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

        //recupero os clientes salvos no banco
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS);
        valueEventListenerPedidos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //limpo minha lista de clientes
                listaPedidos.clear();

                if (snapshot.getValue() != null) {
                    //listar clientes
                    for (DataSnapshot pedidosBanco : snapshot.getChildren()) {
                        Pedidos pedidos = pedidosBanco.getValue(Pedidos.class);
                        listaPedidos.add(pedidos);
                    }

                    tvNaoExistePedido.setVisibility(View.GONE);
                    recyclerViewPedidos.setVisibility(View.VISIBLE);
                } else {
                    tvNaoExistePedido.setVisibility(View.VISIBLE);
                    recyclerViewPedidos.setVisibility(View.GONE);
                }

                //Recupero os dados do sistema e atualizo a lista de clientes e salvo nos dados
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

        adapter = new AdapterPedidos(getActivity(), listaPedidos);
        recyclerViewPedidos = view.findViewById(R.id.recyclerview_pedido_pedido);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPedidos.setAdapter(adapter);

        return view;
    }
}