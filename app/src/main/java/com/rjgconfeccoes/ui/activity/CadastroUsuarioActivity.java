package com.rjgconfeccoes.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.rjgconfeccoes.R;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.model.Usuario;
import com.rjgconfeccoes.ui.util.Base64Custom;
import com.rjgconfeccoes.ui.util.Preferencias;
import com.rjgconfeccoes.ui.util.Util;

import java.util.HashMap;

import static com.rjgconfeccoes.ui.Const.Constantes.CHAVE_EMAIL_PREFERENCIAS;
import static com.rjgconfeccoes.ui.Const.Constantes.CHAVE_SENHA_PREFERENCIAS;
import static com.rjgconfeccoes.ui.Const.Constantes.CHAVE_TESTE;

public class CadastroUsuarioActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Novo Usuario";
    private ConstraintLayout constraintLayout;
    private Toolbar toolbar;
    private EditText nome;
    private EditText email;
    private EditText senha;
    private CheckBox checkBoxPodeCadastrar;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        inicializaCampos();
        configuraToolbar();
        configuraBotaoCadastrar();
    }

    private void configuraToolbar() {
        setTitle(TITULO_APPBAR);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_voltar);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void inicializaCampos() {
        constraintLayout = findViewById(R.id.constraint_layout_cadastrar_usuario);
        nome = findViewById(R.id.et_nome_cadastrar_usuario);
        email = findViewById(R.id.et_email_cadastrar_usuario);
        senha = findViewById(R.id.et_senha_cadastrar_usuario);
        checkBoxPodeCadastrar = findViewById(R.id.checkBox_permitir_cadastro_cadastrar_usuario);
        toolbar = findViewById(R.id.toolbar);
    }

    private void configuraBotaoCadastrar() {
        Button botaoCadastrarUsuario = findViewById(R.id.bt_gravar_cadastrar_pedido);
        botaoCadastrarUsuario.setOnClickListener(v -> validaCamposDigitados());
    }

    private void validaCamposDigitados() {
        Preferencias preferencias = new Preferencias(this);
        String usuarioLogado = preferencias.getNomeUsuarioLogado();
        if (usuarioLogado.toLowerCase().equals(CHAVE_TESTE)) {
            dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.conta_teste_nao_grava_dados));
            limpaCampos();
        } else {
            String nomeDigitado = nome.getText().toString();
            String emailDigitado = email.getText().toString();
            String senhaDigitada = senha.getText().toString();
            if (nomeDigitado.isEmpty() || emailDigitado.isEmpty() || senhaDigitada.isEmpty()) {
                Util.mensagemDeAlerta(CadastroUsuarioActivity.this, constraintLayout, getString(R.string.msg_erro_campos_cadastro_branco));
            } else {
                preencheCampos(nomeDigitado, emailDigitado, senhaDigitada);
            }
        }
    }

    private void preencheCampos(String nome, String email, String senha) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setPodeCadastrar(validaPermitirCadastro());

        cadastrarUsuario(usuario);
    }

    private void cadastrarUsuario(@Nullable final Usuario usuario) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(CadastroUsuarioActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Util.mensagemDeAlerta(CadastroUsuarioActivity.this, constraintLayout, getString(R.string.msg_sucesso_cadastrar_usuario));

                        //Recupero dados usuario cadastrado
                        String identificadorUsuario = Base64Custom.codificarStringBase64(usuario.getEmail());

                        usuario.setId(identificadorUsuario);
                        usuario.salvar();

                        limpaCampos();
                        deslogaUsuarioEEfetuaLogin();

                    } else {
                        String erroExcecao = "";

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            erroExcecao = getString(R.string.msg_erro_senha_minimo_6);
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erroExcecao = getString(R.string.msg_erro_email_invalido);
                        } catch (FirebaseAuthUserCollisionException e) {
                            erroExcecao = getString(R.string.msg_erro_email_em_uso);
                        } catch (Exception e) {
                            erroExcecao = getString(R.string.msg_erro_cadastrar_usuario);
                            e.printStackTrace();
                        }
                        Util.mensagemDeAlerta(CadastroUsuarioActivity.this, constraintLayout, getString(R.string.msg_erro) + erroExcecao);
                    }
                });
    }


    private boolean validaPermitirCadastro() {
        return checkBoxPodeCadastrar.isChecked();
    }

    /**
     * Desloga o usuario cadastrado que o sistema loga sozinho
     * e efetua o login do usuario que ja estava logado anteriormente
     */
    private void deslogaUsuarioEEfetuaLogin() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signOut();

        //recupero dados do usuario logado salvo nas preferencias
        Preferencias preferencias = new Preferencias(this);
        HashMap<String, String> usuarioLogado = preferencias.getDadosUsuarioLogado();
        String emailSalvo = usuarioLogado.get(CHAVE_EMAIL_PREFERENCIAS);
        String senhaSalva = usuarioLogado.get(CHAVE_SENHA_PREFERENCIAS);

        autenticacao.signInWithEmailAndPassword(emailSalvo, senhaSalva).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Util.mensagemDeAlerta(CadastroUsuarioActivity.this, constraintLayout, getString(R.string.msg_erro_conexao_efetue_login_novamente));
            }
            vaiParaLogin();
        });
    }

    private void vaiParaLogin() {
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void limpaCampos() {
        nome.setText("");
        email.setText("");
        senha.setText("");
        checkBoxPodeCadastrar.setChecked(false);
    }

    public void dialogMensagem(String titulo, String mensagem) {
        new AlertDialog
                .Builder(this)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.botao_msg_ok), null)
                .show();
    }
}