package com.rjgconfeccoes.ui.activity;

import static com.rjgconfeccoes.ui.constantes.Constantes.CHAVE_CARGOS;
import static com.rjgconfeccoes.ui.constantes.Constantes.CHAVE_TESTE;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rjgconfeccoes.R;
import com.rjgconfeccoes.api.client.FirebaseDataHelper;
import com.rjgconfeccoes.api.client.OnDataFetchedListener;
import com.rjgconfeccoes.model.Cargo;
import com.rjgconfeccoes.model.Usuario;
import com.rjgconfeccoes.ui.custons.CustomSpinner;
import com.rjgconfeccoes.ui.util.Preferencias;
import com.rjgconfeccoes.ui.util.Util;

import java.util.List;

public class CadastroUsuarioActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Novo Usuario";
    private ConstraintLayout constraintLayout;
    private Toolbar toolbar;
    private EditText nome;
    private EditText cpf;
    private EditText email;
    private EditText senha;
    private CustomSpinner spinnerCargos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        inicializaCampos();
        carregaDados();
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
        nome = findViewById(R.id.form_usuario_nome);
        cpf = findViewById(R.id.form_usuario_cpf);
        email = findViewById(R.id.form_usuario_email);
        senha = findViewById(R.id.form_usuario_senha);
        spinnerCargos = findViewById(R.id.form_usuario_cargo);
        toolbar = findViewById(R.id.toolbar);
    }

    private void carregaDados() {
        obtemDadosSpinnerCargos();
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
            String cpfDigitado = cpf.getText().toString();
            Cargo cargoSelecionado = (Cargo) spinnerCargos.getSelectedItem();
            String emailDigitado = email.getText().toString();
            String senhaDigitada = senha.getText().toString();

//            if (nomeDigitado.isEmpty() || cpfDigitado.isEmpty() || cargoSelecionado.isEmpty() || emailDigitado.isEmpty() || senhaDigitada.isEmpty()) {
//            }
            if (validaCampoVazio(nomeDigitado, cpfDigitado, cargoSelecionado.getFuncao(), emailDigitado, senhaDigitada)) {
                Util.mensagemDeAlerta(CadastroUsuarioActivity.this, constraintLayout, getString(R.string.msg_erro_campos_cadastro_branco));
            } else {
                preencheCampos(nomeDigitado, cpfDigitado, cargoSelecionado, emailDigitado, senhaDigitada);
            }
        }
    }

    private boolean validaCampoVazio(String... campo) {

        for (String s : campo) {
            if (s.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void preencheCampos(String nome, String cpf, Cargo cargo, String email, String senha) {
        try {

            Usuario usuario = new Usuario(nome, cpf, cargo, email, senha);
            cadastrarUsuario(usuario);

        } catch (Exception err) {
            Util.mensagemDeAlerta(this, constraintLayout, err.getMessage());
        }
    }

    private void cadastrarUsuario(@Nullable final Usuario usuario) {
        Util.mensagemDeAlerta(this, constraintLayout, "sucesso");

//        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
//        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
//                .addOnCompleteListener(CadastroUsuarioActivity.this, task -> {
//                    if (task.isSuccessful()) {
//                        Util.mensagemDeAlerta(CadastroUsuarioActivity.this, constraintLayout, getString(R.string.msg_sucesso_cadastrar_usuario));
//
//                        //Recupero dados usuario cadastrado
//                        String identificadorUsuario = Base64Custom.codificarStringBase64(usuario.getEmail());
//
//                        usuario.setId(identificadorUsuario);
//                        usuario.salvar();
//
//                        limpaCampos();
//                        deslogaUsuarioEEfetuaLogin();
//
//                    } else {
//                        String erroExcecao = "";
//
//                        try {
//                            throw task.getException();
//                        } catch (FirebaseAuthWeakPasswordException e) {
//                            erroExcecao = getString(R.string.msg_erro_senha_minimo_6);
//                        } catch (FirebaseAuthInvalidCredentialsException e) {
//                            erroExcecao = getString(R.string.msg_erro_email_invalido);
//                        } catch (FirebaseAuthUserCollisionException e) {
//                            erroExcecao = getString(R.string.msg_erro_email_em_uso);
//                        } catch (Exception e) {
//                            erroExcecao = getString(R.string.msg_erro_cadastrar_usuario);
//                            e.printStackTrace();
//                        }
//                        Util.mensagemDeAlerta(CadastroUsuarioActivity.this, constraintLayout, getString(R.string.msg_erro) + erroExcecao);
//                    }
//                });
    }

    /**
     * Desloga o usuario cadastrado que o sistema loga sozinho
     * e efetua o login do usuario que ja estava logado anteriormente
     */
//    private void deslogaUsuarioEEfetuaLogin() {
//        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
//        autenticacao.signOut();
//
//        //recupero dados do usuario logado salvo nas preferencias
//        Preferencias preferencias = new Preferencias(this);
//        HashMap<String, String> usuarioLogado = preferencias.getDadosUsuarioLogado();
//        String emailSalvo = usuarioLogado.get(CHAVE_EMAIL_PREFERENCIAS);
//        String senhaSalva = usuarioLogado.get(CHAVE_SENHA_PREFERENCIAS);
//
//        autenticacao.signInWithEmailAndPassword(emailSalvo, senhaSalva).addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Util.mensagemDeAlerta(CadastroUsuarioActivity.this, constraintLayout, getString(R.string.msg_erro_conexao_efetue_login_novamente));
//            }
//            vaiParaLogin();
//        });
//    }
    private void vaiParaLogin() {
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void limpaCampos() {
        nome.setText("");
        cpf.setText("");
        email.setText("");
        senha.setText("");
        spinnerCargos.setSelection(0);
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

    private void obtemDadosSpinnerCargos() {
        FirebaseDataHelper.buscaListaDados(CHAVE_CARGOS, Cargo.class, new OnDataFetchedListener<List<Cargo>>() {
            @Override
            public void onDataFetched(List<Cargo> data) {

                data.add(0, new Cargo(0, "Selecione um cargo!"));

                configuraSpinnerCargos(data);
            }

            @Override
            public void onCancelled(Exception ex) {
                Util.mensagemDeAlerta(CadastroUsuarioActivity.this, constraintLayout, ex.getMessage());
            }
        });
    }

    private void configuraSpinnerCargos(List<Cargo> data) {
        SpinnerAdapter cargosAdapter = new ArrayAdapter<Cargo>(
                CadastroUsuarioActivity.this,
                R.layout.spinner_item,
                data);

        spinnerCargos.setAdapter(cargosAdapter);

        spinnerCargos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCargos.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onPopupWindowOpened(Spinner spinner) {
                spinner.setBackground(getResources().getDrawable(R.drawable.bg_spinner_opened));
            }

            @Override
            public void onPopupWindowClosed(Spinner spinner) {
                spinner.setBackground(getResources().getDrawable(R.drawable.bg_spinner_closed));
            }
        });
    }
}