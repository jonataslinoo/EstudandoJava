package com.rjgconfeccoes.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.ui.util.Util;

public class Produto {

    private String id;
    private String descricao;
    private int quantidadeMasculina;
    private int quantidadeFeminina;
    private int quantidadeTotalProdutoPedido;
    private double preco;

    public Produto() {
    }

    public void salvar() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference.child(Util.PRODUTOS).child(getId()).setValue(this);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidadeMasculina() {
        return quantidadeMasculina;
    }

    public void setQuantidadeMasculina(int quantidadeMasculina) {
        this.quantidadeMasculina = quantidadeMasculina;
    }

    public int getQuantidadeFeminina() {
        return quantidadeFeminina;
    }

    public void setQuantidadeFeminina(int quantidadeFeminina) {
        this.quantidadeFeminina = quantidadeFeminina;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Exclude
    public int getQuantidadeTotalProdutoPedido() {
        return quantidadeTotalProdutoPedido;
    }

    public void setQuantidadeTotalProdutoPedido(int quantidadeTotalProdutoPedido) {
        this.quantidadeTotalProdutoPedido = quantidadeTotalProdutoPedido;
    }

}
