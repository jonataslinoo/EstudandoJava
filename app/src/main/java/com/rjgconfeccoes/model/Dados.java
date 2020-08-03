package com.rjgconfeccoes.model;

import java.util.ArrayList;

public class Dados {

    private Usuario usuario;
    private Cliente cliente;
    private Pedidos pedidos;
    private Produto produto;
    private PedidosFinalizados pedidosFinalizados;

    private ArrayList<Cliente> listaClientes;
    private ArrayList<Pedidos> listaPedidos;
    private ArrayList<Produto> listaProdutos;
    private ArrayList<Produto> listaProdutosPedido;
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
     * retorna uma lista com os pedidos cadastrados
     */
    public ArrayList<Pedidos> obtemListaPedidos() {
        if (listaPedidos == null) {
            listaPedidos = new ArrayList<>();
        }
        return listaPedidos;
    }

    /**
     * retorna uma lista com os produtos para pedidos
     */
    public ArrayList<Produto> obtemListaProdutosPedido() {
        if (listaProdutosPedido == null) {
            listaProdutosPedido = new ArrayList<>();
        }
        return listaProdutosPedido;
    }

    /**
     * retorna uma lista com os produtos selecionados para o pedido
     */
    public ArrayList<Produto> obtemListaProdutosSelecionados() {
        if (listaProdutosSelecionados == null) {
            listaProdutosSelecionados = new ArrayList<>();
        }
        return listaProdutosSelecionados;
    }
}
