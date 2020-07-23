package com.rjgconfeccoes.model;

import com.google.firebase.database.DatabaseReference;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.ui.util.Util;

public class Pedidos {

    private String id;
    private String clienteId;
    private String produtoId;
    private int quantidadeTotalProdutos;
    private double valorTotalPedido;

    public void salva(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference.child(Util.PEDIDOS).push().setValue(this);
    }

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

    public String getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(String produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidadeTotalProdutos() {
        return quantidadeTotalProdutos;
    }

    public void setQuantidadeTotalProdutos(int quantidadeTotalProdutos) {
        this.quantidadeTotalProdutos = quantidadeTotalProdutos;
    }

    public double getValorTotalPedido() {
        return valorTotalPedido;
    }

    public void setValorTotalPedido(double valorTotalPedido) {
        this.valorTotalPedido = valorTotalPedido;
    }
}
