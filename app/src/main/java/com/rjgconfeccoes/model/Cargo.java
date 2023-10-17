package com.rjgconfeccoes.model;

import java.io.Serializable;

public class Cargo implements Serializable {

    private int id;
    private String funcao;

    private Cargo() {
    }

    public Cargo(int id, String descricao) {
        this.id = id;
        this.funcao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getFuncao() {
        return funcao;
    }

    @Override
    public String toString() {
       return Character.toUpperCase(funcao.charAt(0)) + funcao.substring(1).toLowerCase();
    }
}
