package com.rjgconfeccoes.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

public class Pedidos {

    private String id;
    private String clienteId;
    private ArrayList<ProdutoPedido> listaProdutosPedido;

    public Pedidos() {
    }

    public void salvar() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference.child(Util.PEDIDOS).child(getId());
        databaseReference.child(Util.PEDIDOS).child(getId()).setValue("clienteId", getClienteId());

    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    @Exclude
    public ArrayList<ProdutoPedido> getListaProdutosPedido() {
        return listaProdutosPedido;
    }

    public void setListaProdutosPedido(ArrayList<ProdutoPedido> listaProdutosPedido) {
        this.listaProdutosPedido = listaProdutosPedido;
    }
}
