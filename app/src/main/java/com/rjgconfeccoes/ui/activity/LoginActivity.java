package com.rjgconfeccoes.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rjgconfeccoes.R;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.model.Cliente;
import com.rjgconfeccoes.model.Dados;
import com.rjgconfeccoes.model.Pedido;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.model.ProdutoPedido;
import com.rjgconfeccoes.model.Usuario;
import com.rjgconfeccoes.ui.util.Base64Custom;
import com.rjgconfeccoes.ui.util.Preferencias;
import com.rjgconfeccoes.ui.util.Util;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private FirebaseAuth autenticacao;
    private DatabaseReference databaseReference;
    private ConstraintLayout constraintLayout;
    private ArrayList<Pedido> listaPedidos;
    private ArrayList<ProdutoPedido> listaProdutosPedido;
    private ArrayList<Pedido> listaPedidoFinalizados;
    private ArrayList<ProdutoPedido> listaProdutosPedidoFinalizado;
    private ArrayList<Cliente> listaClientes;
    private ArrayList<Produto> listaProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializaCampos();
        validaUsuarioLogado();
        configuraBotaoLogin();

        //todo: remover quando tudo estiver rodando legal
        View viewById = findViewById(R.id.titulo_principal_login);
        viewById.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
            startActivity(intent);
        });
    }

    private void inicializaCampos() {
        constraintLayout = findViewById(R.id.constraint_layout_login);
        email = findViewById(R.id.et_email_login);
        senha = findViewById(R.id.et_senha_login);
    }

    private void validaUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            vaiParaTelaDeDashboard();
        }
    }

    private void configuraBotaoLogin() {
        Button botaoEfutarLogin = findViewById(R.id.bt_login_login);
        botaoEfutarLogin.setOnClickListener(v -> validaCamposDigitados());
    }

    private void validaCamposDigitados() {
        String emailDigitado = email.getText().toString();
        String senhaDigitada = senha.getText().toString();
        if (emailDigitado.isEmpty() || senhaDigitada.isEmpty()) {
            Util.mensagemDeAlerta(this, constraintLayout, getString(R.string.msg_erro_campos_login_branco));
        } else {
            preencheCampos(emailDigitado, senhaDigitada);
        }
    }

    private void preencheCampos(String emailDigitado, String senhaDigitada) {
        Usuario usuario = new Usuario();
        usuario.setEmail(emailDigitado);
        usuario.setSenha(senhaDigitada);

        validarLogin(usuario);
    }

    private void validarLogin(final Usuario usuario) {
        String identificacaoUsuario = Base64Custom.codificarStringBase64(usuario.getEmail());

        //recupero a referencia de autencicação do firebase
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                //Recuperar instancia do firebase recebendo o usuario logado pelo identificador
                databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.USUARIOS).child(identificacaoUsuario);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {

                            //recupero os dados do usuario que estão salvos na base do firebase
                            Usuario usuarioLogado = dataSnapshot.getValue(Usuario.class);
                            salvaUsuarioNasPreferencias(usuarioLogado);
                        } else {
                            Util.mensagemDeAlerta(LoginActivity.this, constraintLayout, getString(R.string.msg_erro_dados_usuario_nao_encontrados));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Util.mensagemDeAlerta(LoginActivity.this, constraintLayout, getString(R.string.msg_sucesso_login));
                vaiParaTelaDeDashboard();

            } else {
                String erroExcecao = "";

                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    erroExcecao = getString(R.string.msg_erro_email_nao_existe_desabilitado);
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    erroExcecao = getString(R.string.msg_erro_senha_incorreta);
                } catch (Exception e) {
                    erroExcecao = e.getMessage();
                }
                Util.mensagemDeAlerta(LoginActivity.this, constraintLayout, getString(R.string.msg_erro) + erroExcecao);
            }
        });
    }

    private void salvaUsuarioNasPreferencias(Usuario usuario) {
        Preferencias preferencias = new Preferencias(LoginActivity.this);
//        String emailCodificadoUsuarioLogado = Base64Custom.codificarStringBase64(usuario.getEmail());
        preferencias.salvarDadosUsuario(usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.isPodeCadastrar());
    }

    private void vaiParaTelaDeDashboard() {
        recuperaDadosPedidos();
        recuperaDadosPedidosFinalizados();
        recuperaDadosClientes();
        recuperaDadosProdutos();

        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void recuperaDadosPedidos() {
        DatabaseReference databaseReferencePedidos = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS);

        databaseReferencePedidos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listaPedidos = new ArrayList<>();

                if (snapshot.getValue() != null) {

                    //listar Pedidos
                    for (DataSnapshot idPedidoBanco : snapshot.getChildren()) {
                        String chaveGeral = idPedidoBanco.getKey();
                        Pedido pedido = new Pedido();

                        listaProdutosPedido = new ArrayList<>();

                        for (DataSnapshot produtosPedidoBanco : snapshot.child(chaveGeral).getChildren()) {
                            ProdutoPedido produtoPedido = produtosPedidoBanco.getValue(ProdutoPedido.class);
                            listaProdutosPedido.add(produtoPedido);
                        }

                        String[] identificadores = idPedidoBanco.getKey().split(";");
                        String idCliente = identificadores[0];
                        String idPedido = identificadores[1];

                        pedido.setId(idPedido);
                        pedido.setClienteId(idCliente);
                        pedido.setListaProdutosPedido(listaProdutosPedido);
                        listaPedidos.add(pedido);
                    }

                    Dados dados = Util.recuperaDados();
                    dados.obtemListaPedidos().clear();
                    dados.obtemListaPedidos().addAll(listaPedidos);
                    Util.defineDados(dados);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaDadosPedidosFinalizados() {
        DatabaseReference databaseReferencePedidosFinalizados = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PEDIDOS_FINALIZADOS);

        databaseReferencePedidosFinalizados.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listaPedidoFinalizados = new ArrayList<>();

                if (snapshot.getValue() != null) {

                    //listar Pedidos Finalizados
                    for (DataSnapshot idPedidoBanco : snapshot.getChildren()) {
                        String chaveGeral = idPedidoBanco.getKey();
                        Pedido pedido = new Pedido();

                        listaProdutosPedidoFinalizado = new ArrayList<>();

                        for (DataSnapshot produtosPedidoBanco : snapshot.child(chaveGeral).getChildren()) {
                            ProdutoPedido produtoPedido = produtosPedidoBanco.getValue(ProdutoPedido.class);
                            listaProdutosPedidoFinalizado.add(produtoPedido);
                        }

                        String[] identificadores = idPedidoBanco.getKey().split(";");
                        String idCliente = identificadores[0];
                        String idPedido = idPedidoBanco.getKey();

                        pedido.setId(idPedido);
                        pedido.setClienteId(idCliente);
                        pedido.setListaProdutosPedido(listaProdutosPedidoFinalizado);
                        listaPedidoFinalizados.add(pedido);
                    }

                    Dados dados = Util.recuperaDados();
                    dados.obtemListaPedidosFinalizados().clear();
                    dados.obtemListaPedidosFinalizados().addAll(listaPedidoFinalizados);
                    Util.defineDados(dados);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaDadosClientes() {
        DatabaseReference databaseReferenceClientes = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.CLIENTES);

        databaseReferenceClientes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listaClientes = new ArrayList<>();

                if (snapshot.getValue() != null) {

                    //listar clientes
                    for (DataSnapshot clientesBanco : snapshot.getChildren()) {
                        Cliente cliente = clientesBanco.getValue(Cliente.class);
                        listaClientes.add(cliente);
                    }

                    //Recupero os dados do sistema e atualizo a lista de clientes e salvo nos dados
                    Dados dados = Util.recuperaDados();
                    dados.obtemListaClientes().clear();
                    dados.obtemListaClientes().addAll(listaClientes);
                    Util.defineDados(dados);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaDadosProdutos() {
        DatabaseReference databaseReferenceProdutos = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PRODUTOS);

        databaseReferenceProdutos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listaProdutos = new ArrayList<>();

                if (snapshot.getValue() != null) {

                    //listar Produtos
                    for (DataSnapshot produtosBanco : snapshot.getChildren()) {
                        Produto produto = produtosBanco.getValue(Produto.class);
                        listaProdutos.add(produto);
                    }

                    //Recupero os dados do sistema e atualizo a lista de Produtos e salvo nos dados
                    Dados dados = Util.recuperaDados();
                    dados.obtemListaProdutos().clear();
                    dados.obtemListaProdutos().addAll(listaProdutos);
                    Util.defineDados(dados);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}