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
import com.rjgconfeccoes.model.Usuario;
import com.rjgconfeccoes.ui.util.Base64Custom;
import com.rjgconfeccoes.ui.util.Preferencias;
import com.rjgconfeccoes.ui.util.Util;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private FirebaseAuth autenticacao;
    private DatabaseReference databaseReference;
    private ConstraintLayout constraintLayout;

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
            vaiParaTelaDePedidos();
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
                databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios").child(identificacaoUsuario);
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
                vaiParaTelaDePedidos();

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
        preferencias.salvarDadosUsuario(usuario.getEmail(), usuario.getSenha(), usuario.isPodeCadastrar());
    }

    private void vaiParaTelaDePedidos() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
