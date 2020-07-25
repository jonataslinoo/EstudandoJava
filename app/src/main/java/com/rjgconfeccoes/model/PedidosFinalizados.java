package com.rjgconfeccoes.model;

import com.google.firebase.database.DatabaseReference;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.ui.util.Util;

public class PedidosFinalizados {

    private String id;
    private String clienteId;
    private String produtoId;
    private int quantidadeProduto;
    private int quantidadeTotal;
    private double valorTotal;

    public PedidosFinalizados() {
    }

    public void salva() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference.child(Util.PEDIDOS_FINALIZADOS).push().setValue(this);
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

    public int getQuantidadeProduto() {
        return quantidadeProduto;
    }

    public void setQuantidadeProduto(int quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(int quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}

