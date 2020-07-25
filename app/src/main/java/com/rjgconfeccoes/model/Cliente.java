package com.rjgconfeccoes.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.ui.util.Util;

public class Cliente {

    private String id;
    private String nome;
    private String telefoneComArea;

    public Cliente() {
    }

    public void salvar() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference.child(Util.CLIENTES).child(getId()).setValue(this);
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

    public String getTelefoneComArea() {
        return telefoneComArea;
    }

    public void setTelefoneComArea(String telefoneComArea) {
        this.telefoneComArea = telefoneComArea;
    }

    @Override
    public String toString() {
        return nome;
    }
}
