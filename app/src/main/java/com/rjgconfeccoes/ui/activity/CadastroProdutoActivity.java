package com.rjgconfeccoes.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rjgconfeccoes.R;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.model.Produto;
import com.rjgconfeccoes.ui.util.Base64Custom;
import com.rjgconfeccoes.ui.util.Util;

public class CadastroProdutoActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "Novo Produto";
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private EditText nome;
    private EditText tipoSexo;
    private EditText preco;
    private DatabaseReference databaseReference;
    private CheckBox kit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        inicializaCampos();
        configuraToolbar();
        configuraKit();
        configuraBotaoCadastrar();
//        criaFormatacaoTelefone();
    }

    private void inicializaCampos() {
        toolbar = findViewById(R.id.toolbar);
        constraintLayout = findViewById(R.id.constraint_layout_cadastrar_produto);
        nome = findViewById(R.id.et_nome_cadastrar_produto);
        tipoSexo = findViewById(R.id.et_tipo_cadastrar_produto);
        preco = findViewById(R.id.et_preco_cadastrar_produto);
    }

    private void configuraToolbar() {
        setTitle(TITULO_APPBAR);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_voltar);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void configuraKit() {
        kit = findViewById(R.id.checkBox_kit_cadastrar_produto);
        kit.setOnClickListener(v -> verificaProdutoEhKit());
    }

    private void verificaProdutoEhKit() {
        if (kit.isChecked()) {
            tipoSexo.setVisibility(View.INVISIBLE);
            tipoSexo.setText("");
        } else {
            tipoSexo.setVisibility(View.VISIBLE);
        }
    }

    private void configuraBotaoCadastrar() {
        Button botaoCadastrarProduto = findViewById(R.id.bt_cadastrar_cadastrar_produto);
        botaoCadastrarProduto.setOnClickListener(v -> validaCamposDigitados());
    }

    private void validaCamposDigitados() {
        String nomeDigitado = nome.getText().toString();
        String tipoSexoDigitada = tipoSexo.getText().toString();
        double precoDigitado = Double.parseDouble(preco.getText().toString());
        boolean kitMarcadao = kit.isChecked();
        if (!kitMarcadao) {
            if (nomeDigitado.isEmpty() || tipoSexoDigitada.isEmpty() || precoDigitado == 0) {
                Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_erro_campos_cadastro_branco));
            } else {
                preencheCampos(nomeDigitado, tipoSexoDigitada, precoDigitado);
            }
        } else {
            if (nomeDigitado.isEmpty() || precoDigitado == 0) {
                Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_erro_campos_cadastro_branco));
            } else {
                preencheCampos(nomeDigitado, tipoSexoDigitada, precoDigitado);
            }
        }
    }

    private void preencheCampos(String nome, String tipoSexo, double preco) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setTipoSexo(tipoSexo);
        produto.setPreco(preco);

        salvaProduto(produto);


//        String formataTelefone;
//        if (telefoneSemFormatacao.length() == 11) {
//            formataTelefone = telefoneSemFormatacao.substring(0, 3) + " " + telefoneSemFormatacao.substring(3, 7) + "-" + telefoneSemFormatacao.substring(7);
//        } else {
//            formataTelefone = telefoneSemFormatacao.substring(0, 3) + " " + telefoneSemFormatacao.substring(3, 8) + "-" + telefoneSemFormatacao.substring(8);
//        }
    }

    private void salvaProduto(Produto produto) {
        //codifica o telefone em base64 para usar como id do cliente
        String idProduto = Base64Custom.codificarStringBase64(produto.getNome()+produto.getTipoSexo());

        //Instancio uma referencia ao banco de dados
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child("produtos").child(idProduto);

        //Recupero o cliente pelo id caso exista
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //se existir cliente com o mesmo id
                if (snapshot.getValue() != null) {
                    //retorno com mensagem ja possui cadastro
                    Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_erro_produto_ja_cadastrado));
                } else {
                    //defino o id do cliente e salvo no banco
                    produto.setId(idProduto);
                    produto.salvar();

                    Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_sucesso_cadastrar_produto));
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
        tipoSexo.setText("");
        preco.setText("");
    }

    private void vaiParaTelaDashboard() {
//        Intent intent = new Intent(CadastroClienteActivity.this, DashboardActivity.class);
//        startActivity(intent);
        finish();
    }
}