package com.rjgconfeccoes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Cliente;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

public class AdapterClientesFragment extends RecyclerView.Adapter<AdapterClientesFragment.ViewHolderClientes> {

    private final Context context;
    private final List<Cliente> listClientes;

    public AdapterClientesFragment(Context context, List<Cliente> listClientes) {
        this.context = context;
        this.listClientes = listClientes;
    }

    @NonNull
    @Override
    public ViewHolderClientes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.lst_clientes_fragment, parent, false);
        return new ViewHolderClientes(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClientes holder, int position) {
        Collections.sort(listClientes, (cliente, cliente2) -> cliente.getNome().compareTo(cliente2.getNome()));
        Cliente cliente = listClientes.get(position);
        holder.vincula(cliente);
    }

    @Override
    public int getItemCount() {
        return listClientes.size();
    }

    public class ViewHolderClientes extends RecyclerView.ViewHolder {

        public final TextView nomeCliente;
        public final TextView telefoneCliente;

        public ViewHolderClientes(@NonNull View itemView) {
            super(itemView);

            nomeCliente = itemView.findViewById(R.id.tv_nome_cliente_fragment);
            telefoneCliente = itemView.findViewById(R.id.tv_telefone_cliente_fragment);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }

        public void vincula(Cliente cliente) {
//            this.cliente = cliente;
//            preencheCampos(cliente);

            nomeCliente.setText(cliente.getNome());
            telefoneCliente.setText(cliente.getTelefoneComArea());
        }

        private void preencheCampos(Cliente cliente) {
            nomeCliente.setText(cliente.getNome());
            telefoneCliente.setText(cliente.getTelefoneComArea());
        }
    }
}

