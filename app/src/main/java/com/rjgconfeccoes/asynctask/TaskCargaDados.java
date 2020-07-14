package com.rjgconfeccoes.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.model.Cliente;
import com.rjgconfeccoes.ui.adapters.AdapterClientesFragment;
import com.rjgconfeccoes.ui.util.Util;

import java.util.List;

public class TaskCargaDados extends AsyncTask<ValueEventListener, Void, ValueEventListener> {

    //Variaveis da classe
    private Context context;
    private AlertDialog alertDialog;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerContatos;
    private List<Cliente> listClientes;
    private AdapterClientesFragment adapter;
    private BuscaClientesListener listener;

    /**
     * Construtor da classe
     */
    public TaskCargaDados(Context context, List<Cliente> listClientes, AdapterClientesFragment adapter, BuscaClientesListener listener) {
        this.context = context;
        this.listClientes = listClientes;
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        //Cria o alert dialog com progressBar
        alertDialog = Util.criaProgressBar(context);
        alertDialog.show();
    }

    @Override
    public ValueEventListener doInBackground(ValueEventListener... valueEventListeners) {

        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child("clientes");
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

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        return valueEventListenerContatos;
    }

    @Override
    protected void onPostExecute(ValueEventListener valueEventListener) {
        try {
            listener.clientesEncontrados(valueEventListener);
        } finally {
            Util.escondeProgressBar(alertDialog);
        }
    }

    public interface BuscaClientesListener {
        void clientesEncontrados(ValueEventListener valueEventListener);
    }
}