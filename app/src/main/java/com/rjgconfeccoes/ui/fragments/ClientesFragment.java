package com.rjgconfeccoes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.rjgconfeccoes.model.Cliente;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.ui.adapters.AdapterClientesFragment;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;
import java.util.List;


public class ClientesFragment extends Fragment {

    private AdapterClientesFragment adapter;
    private ArrayList<Cliente> listClientes = new ArrayList<>();
    private RecyclerView recyclerViewClientes;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerContatos;
    private AlertDialog alertDialog;

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerContatos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        alertDialog = Util.criaProgressBar(getContext(), "Carregando Clientes");
        alertDialog.show();

        //recupero os clientes salvos no banco
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.CLIENTES);
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //limpo minha lista de clientes
                listClientes.clear();

                //listar clientes
                for (DataSnapshot clientesBanco : snapshot.getChildren()) {
                    Cliente cliente = clientesBanco.getValue(Cliente.class);
                    listClientes.add(cliente);
                }

                //Recupero os dados do sistema e atualizo a lista de clientes e salvo nos dados
                Dados dados = Util.recuperaDados();
                dados.obtemListaClientes().clear();
                dados.obtemListaClientes().addAll(listClientes);
                Util.defineDados(dados);

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
        adapter = new AdapterClientesFragment(getActivity(), listClientes);
        recyclerViewClientes = view.findViewById(R.id.recyclerview_clientes);
        recyclerViewClientes.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewClientes.setAdapter(adapter);
    }
}