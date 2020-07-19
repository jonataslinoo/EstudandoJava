package com.rjgconfeccoes.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.ui.util.Util;

public class Produto {

    private String id;
    private String nome;
    private int quantidadeMasculina;
    private int quantidadeFeminina;
    private double preco;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
}
