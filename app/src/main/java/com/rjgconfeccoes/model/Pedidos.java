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
    private String valorTotalPedido;

    public Pedidos() {
    }

    public void salvar() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
//        databaseReference.child(Util.PEDIDOS).child(getId()).setValue(this);
        databaseReference.child(Util.PEDIDOS).child(getId());

//        databaseReference.child(Util.PEDIDOS).child(getId()).setValue(getListaProdutosPedido());
//        databaseReference.child(Util.PEDIDOS).child(getId()).setValue(getValorTotalPedido());

        databaseReference.child(Util.PEDIDOS).child(getId()).setValue("clienteId", getClienteId());
//        databaseReference.setValue("valorTotalPedido", getValorTotalPedido());
//        for (ProdutoPedido produtoPedido : getListaProdutosPedido()) {
//
//            databaseReference.setValue(produtoPedido.getProdutoId());
//            databaseReference.setValue(produtoPedido.getQuantidadeTotalProdutos());
////            databaseReference.setValue("produtoId", produtoPedido.getProdutoId());
////            databaseReference.setValue("quantidadeTotalProdutos", produtoPedido.getQuantidadeTotalProdutos());
//        }

    }
//
//    public ArrayList<ProdutoPedido> obtemListaProdutosPedido() {
//
//        if (listaProdutosPedido == null) {
//            listaProdutosPedido = new ArrayList<>();
//        }
//        return listaProdutosPedido;
//    }

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

    public String getValorTotalPedido() {
        return valorTotalPedido;
    }

    public void setValorTotalPedido(String valorTotalPedido) {
        this.valorTotalPedido = valorTotalPedido;
    }

    @Exclude
    public ArrayList<ProdutoPedido> getListaProdutosPedido() {
        if (listaProdutosPedido == null) {
            listaProdutosPedido = new ArrayList<>();
        }
        return listaProdutosPedido;
    }

    public void setListaProdutosPedido(ArrayList<ProdutoPedido> listaProdutosPedido) {
        this.listaProdutosPedido = listaProdutosPedido;
    }
}
