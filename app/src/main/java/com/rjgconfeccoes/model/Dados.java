package com.rjgconfeccoes.model;

import java.util.ArrayList;

public class Dados {

    private Usuario usuario;
    private Cliente cliente;
    private Pedidos pedidos;
    private PedidosFinalizados pedidosFinalizados;
    private Produto produto;

    private ArrayList<Cliente> listaClientes;
    private ArrayList<Produto> listaProdutos;
    private ArrayList<Produto> listaProdutosSelecionados;

    /**
     * retorna uma lista com os clientes cadastrados
     */
    public ArrayList<Cliente> obtemListaClientes() {
        if (listaClientes == null) {
            listaClientes = new ArrayList<>();
        }
        return listaClientes;
    }

    /**
     * retorna uma lista com os produtos cadastrados
     */
    public ArrayList<Produto> obtemListaProdutos() {
        if (listaProdutos == null) {
            listaProdutos = new ArrayList<>();
        }
        return listaProdutos;
    }

    /**
     * retorna uma lista com os produtos cadastrados
     */
    public ArrayList<Produto> obtemListaProdutosSelecionados() {
        if (listaProdutosSelecionados == null) {
            listaProdutosSelecionados = new ArrayList<>();
        }
        return listaProdutosSelecionados;
    }
}
