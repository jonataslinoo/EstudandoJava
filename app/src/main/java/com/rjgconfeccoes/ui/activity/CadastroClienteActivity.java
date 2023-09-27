package com.rjgconfeccoes.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rjgconfeccoes.R;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.model.Cliente;
import com.rjgconfeccoes.ui.util.Base64Custom;
import com.rjgconfeccoes.ui.util.Preferencias;
import com.rjgconfeccoes.ui.util.Util;

import static com.rjgconfeccoes.ui.Const.Constantes.CHAVE_TESTE;

public class CadastroClienteActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "Novo Cliente";
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private EditText nome;
    private EditText area;
    private EditText telefone;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);

        inicializaCampos();
        configuraToolbar();
        configuraBotaoCadastrar();
        criaFormatacaoTelefone();
    }

    private void inicializaCampos() {
        toolbar = findViewById(R.id.toolbar);
        constraintLayout = findViewById(R.id.constraint_layout_cadastrar_cliente);
        nome = findViewById(R.id.et_nome_cadastrar_cliente);
        area = findViewById(R.id.et_area_cadastrar_cliente);
        telefone = findViewById(R.id.et_telefone_cadastrar_cliente);
    }

    private void configuraToolbar() {
        setTitle(TITULO_APPBAR);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_voltar);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void configuraBotaoCadastrar() {
        Button botaoCadastrarUsuario = findViewById(R.id.bt_cadastrar_cadastrar_cliente);
        botaoCadastrarUsuario.setOnClickListener(v -> validaCamposDigitados());
    }

    private void validaCamposDigitados() {
        Preferencias preferencias = new Preferencias(this);
        String usuarioLogado = preferencias.getNomeUsuarioLogado();
        if (usuarioLogado.toLowerCase().equals(CHAVE_TESTE)) {
            dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.conta_teste_nao_grava_dados));
        } else {
            String nomeDigitado = nome.getText().toString();
            String areaDigitada = area.getText().toString();
            String telefoneDigitado = telefone.getText().toString();
            if (nomeDigitado.isEmpty() || areaDigitada.isEmpty() || telefoneDigitado.isEmpty()) {
                Util.mensagemDeAlerta(CadastroClienteActivity.this, constraintLayout, getString(R.string.msg_erro_campos_cadastro_branco));
            } else {
                preencheCampos(nomeDigitado, areaDigitada, telefoneDigitado);
            }
        }
    }

    private void preencheCampos(String nome, String area, String telefone) {
        //Fomata telefone para salvar
        String telefoneSemFormatacao = area + telefone.replace("-", "");

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setTelefoneComArea(telefoneSemFormatacao);

        salvaCliente(cliente);


//        String formataTelefone;
//        if (telefoneSemFormatacao.length() == 11) {
//            formataTelefone = telefoneSemFormatacao.substring(0, 3) + " " + telefoneSemFormatacao.substring(3, 7) + "-" + telefoneSemFormatacao.substring(7);
//        } else {
//            formataTelefone = telefoneSemFormatacao.substring(0, 3) + " " + telefoneSemFormatacao.substring(3, 8) + "-" + telefoneSemFormatacao.substring(8);
//        }
    }

    private void salvaCliente(Cliente cliente) {
        //codifica o telefone em base64 para usar como id do cliente
        String idCliente = Base64Custom.codificarStringBase64(cliente.getTelefoneComArea());

        //Instancio uma referencia ao banco de dados
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.CLIENTES).child(idCliente);

        //Recupero o cliente pelo id caso exista
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //se existir cliente com o mesmo id
                if (snapshot.getValue() != null) {
                    //retorno com mensagem ja possui cadastro
                    Util.mensagemDeAlerta(CadastroClienteActivity.this, constraintLayout, getString(R.string.msg_erro_cliente_ja_cadastrado));
                } else {
                    //defino o id do cliente e salvo no banco
                    cliente.setId(idCliente);
                    cliente.salvar();

                    Util.mensagemDeAlerta(CadastroClienteActivity.this, constraintLayout, getString(R.string.msg_sucesso_cadastrar_cliente));
                    limpaCampos();
                    vaiParaTelaDashboard();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void limpaCampos() {
        nome.setText("");
        area.setText("");
        telefone.setText("");
    }

    private void vaiParaTelaDashboard() {
        finish();
    }

    private void criaFormatacaoTelefone() {
        SimpleMaskFormatter simpleMaskArea = new SimpleMaskFormatter("NNN");
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher maskTextArea = new MaskTextWatcher(area, simpleMaskArea);
        MaskTextWatcher maskTexTelefone = new MaskTextWatcher(telefone, simpleMaskTelefone);
        area.addTextChangedListener(maskTextArea);
        telefone.addTextChangedListener(maskTexTelefone);
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