package com.rjgconfeccoes.model;

import java.util.ArrayList;

public class Dados {

    public Usuario usuario;
    public Cliente cliente;
    public Pedidos pedidos;
    public PedidosFinalizados pedidosFinalizados;
    public Produto produto;

    public ArrayList<Cliente> listaClientes;

    /**
     * retorna uma lista com os clientes cadastrados
     */
    public ArrayList<Cliente> obtemListaClientes() {
        if (listaClientes == null) {
            listaClientes = new ArrayList<>();
        }
        return listaClientes;
    }
}
