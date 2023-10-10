package com.rjgconfeccoes.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.rjgconfeccoes.ui.util.Preferencias;
import com.rjgconfeccoes.ui.util.Util;

import static com.rjgconfeccoes.ui.constantes.Constantes.CHAVE_TESTE;

public class CadastroProdutoActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "Novo Produto";
    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private EditText nome;
    private TextView qtdeMasculina;
    private TextView qtdeFeminina;
    private EditText preco;
    private CheckBox kitAdulto;
    private CheckBox kitInfantil;
    private ImageView botaoAdicionarMasculina;
    private ImageView botaoRemoverMasculina;
    private ImageView botaoAdicionarFeminina;
    private ImageView botaoRemoverFeminina;

    private int quantidadeClicadaMasculina = 0;
    private int quantidadeClicadaFeminina = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        inicializaCampos();
        configuraToolbar();
        configuraAdicionarRemoverQuantidade();
        configuraBotaoCadastrar();
        configuraKits();
    }

    private void inicializaCampos() {
        toolbar = findViewById(R.id.toolbar);
        constraintLayout = findViewById(R.id.constraint_layout_cadastrar_produto);
        nome = findViewById(R.id.et_nome_cadastrar_produto);
        qtdeMasculina = findViewById(R.id.et_quanditade_masculina_cadastrar_produto);
        qtdeFeminina = findViewById(R.id.et_quantidade_feminina_cadastrar_produto);
        preco = findViewById(R.id.et_preco_cadastrar_produto);
    }

    private void configuraToolbar() {
        setTitle(TITULO_APPBAR);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_voltar);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void configuraKits() {
        kitAdulto = findViewById(R.id.checkBox_kit_adulto_cadastrar_produto);
        kitInfantil = findViewById(R.id.checkBox_kit_infantil_cadastrar_produto);
        //Define os listeners da tela
        kitAdulto.setOnClickListener(this::vailidaKitMarcado);
        kitInfantil.setOnClickListener(this::vailidaKitMarcado);
    }

    private void vailidaKitMarcado(View view) {
        if (view == kitAdulto) {
            kitInfantil.setChecked(false);
        } else if (view == kitInfantil) {
            kitAdulto.setChecked(false);
        }
    }

    private void configuraAdicionarRemoverQuantidade() {
        botaoAdicionarMasculina = findViewById(R.id.botao_adicionar_masculina_cadastrar_produto);
        botaoRemoverMasculina = findViewById(R.id.botao_remover_masculina_cadastrar_produto);
        botaoAdicionarFeminina = findViewById(R.id.botao_adicionar_feminina_cadastrar_produto);
        botaoRemoverFeminina = findViewById(R.id.botao_remover_feminina_cadastrar_produto);
        //Define os listeners da tela
        botaoAdicionarMasculina.setOnClickListener(this::somaQuantidadeClicada);
        botaoRemoverMasculina.setOnClickListener(this::somaQuantidadeClicada);
        botaoAdicionarFeminina.setOnClickListener(this::somaQuantidadeClicada);
        botaoRemoverFeminina.setOnClickListener(this::somaQuantidadeClicada);
    }

    private void somaQuantidadeClicada(View view) {
        if (view == botaoAdicionarMasculina) {
            quantidadeClicadaMasculina++;
        } else if (view == botaoRemoverMasculina) {
            if (quantidadeClicadaMasculina != 0) {
                quantidadeClicadaMasculina--;
            }
        } else if (view == botaoAdicionarFeminina) {
            quantidadeClicadaFeminina++;
        } else if (view == botaoRemoverFeminina) {
            if (quantidadeClicadaMasculina != 0) {
                quantidadeClicadaFeminina--;
            }
        }
        qtdeMasculina.setText(String.valueOf(quantidadeClicadaMasculina));
        qtdeFeminina.setText(String.valueOf(quantidadeClicadaFeminina));
    }

    private void configuraBotaoCadastrar() {
        Button botaoCadastrarProduto = findViewById(R.id.bt_cadastrar_cadastrar_produto);
        botaoCadastrarProduto.setOnClickListener(v -> validaCamposDigitados());
    }

    private void validaCamposDigitados() {
        Preferencias preferencias = new Preferencias(this);
        String usuarioLogado = preferencias.getNomeUsuarioLogado();
        if (usuarioLogado.toLowerCase().equals(CHAVE_TESTE)) {
            dialogMensagem(getString(R.string.titulo_msg_atencao), getString(R.string.conta_teste_nao_grava_dados));
            limpaCampos();
        } else {
            String nomeDigitado = nome.getText().toString();
            int quantidadeMasculina = Integer.parseInt(qtdeMasculina.getText().toString());
            int quantidadeFeminina = Integer.parseInt(qtdeFeminina.getText().toString());
            double precoDigitado = 0;
            boolean kitAdultoMarcado = kitAdulto.isChecked();
            boolean kitInfantilMarcado = kitInfantil.isChecked();
            int quantidadeTotal = 0;

            quantidadeTotal = quantidadeMasculina + quantidadeFeminina;

            if (nomeDigitado.isEmpty()) {
                Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_erro_nome_produto_branco));
                return;
            } else if (quantidadeTotal == 0) {
                Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_erro_quantidade_nao_pode_zero));
                return;
            } else if (!preco.getText().toString().isEmpty()) {
                precoDigitado = Double.parseDouble(preco.getText().toString());

                if (precoDigitado == 0) {
                    Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_erro_valor_produto_maior_que_zero));
                    return;
                }
            } else {
                Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_erro_valor_produto_branco));
                return;
            }

            if (kitAdultoMarcado) {
                if (quantidadeTotal != 3) {
                    Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_erro_quantidade_produtos_kit_adulto));
                    return;
                }
            } else if (kitInfantilMarcado) {
                if (quantidadeTotal != 4) {
                    Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_erro_quantidade_produtos_kit_infantil));
                    return;
                }
            } else {
                if (quantidadeTotal != 1) {
                    Util.mensagemDeAlerta(CadastroProdutoActivity.this, constraintLayout, getString(R.string.msg_erro_quantidade_produtos_tipo));
                    return;
                }
            }
            preencheCampos(nomeDigitado, quantidadeMasculina, quantidadeFeminina, precoDigitado);
        }
    }

    private void preencheCampos(String nome, int quantidadeMasculina, int quantidadeFeminina, double preco) {
        Produto produto = new Produto();
        produto.setDescricao(nome);
        produto.setQuantidadeMasculina(quantidadeMasculina);
        produto.setQuantidadeFeminina(quantidadeFeminina);
        produto.setPreco(preco);

        salvaProduto(produto);
    }

    private void salvaProduto(Produto produto) {
        //codifica o telefone em base64 para usar como id do cliente
        String idProduto = Base64Custom.codificarStringBase64(produto.getDescricao());

        //Instancio uma referencia ao banco de dados
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child(Util.PRODUTOS).child(idProduto);

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
        qtdeMasculina.setText("");
        qtdeFeminina.setText("");
        preco.setText("");
        kitAdulto.setChecked(false);
        kitInfantil.setChecked(false);
    }

    private void vaiParaTelaDashboard() {
        finish();
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