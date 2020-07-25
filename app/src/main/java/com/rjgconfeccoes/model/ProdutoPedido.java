package com.rjgconfeccoes.model;

public class ProdutoPedido {

    private String produtoId;
    private int quantidadeTotalProdutos;

    public ProdutoPedido() {
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
}
