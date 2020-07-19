package com.rjgconfeccoes.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rjgconfeccoes.R;
import com.rjgconfeccoes.ui.fragments.ClientesFragment;
import com.rjgconfeccoes.ui.fragments.OpcoesFragment;
import com.rjgconfeccoes.ui.fragments.PedidosFinalizadosFragment;
import com.rjgconfeccoes.ui.fragments.PedidosFragment;
import com.rjgconfeccoes.ui.fragments.ProdutosFragment;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //Constantes da tela
    public static final int MENU_NOVO_PEDIDO = 1;
    public static final int MENU_PEDIDOS_FINALIZADOS = 2;
    public static final int MENU_NOVO_PRODUTO = 3;
    public static final int MENU_NOVO_CLIENTE = 4;
    public static final int MENU_OPCOES = 5;
    public static final String TITULO_TOOLBAR_PEDIDOS = "Pedidos";
    public static final String TITULO_TOOLBAR_PEDIDOS_FINALIZADOS = "Pedidos Finalizados";
    public static final String TITULO_TOOLBAR_PRODUTOS = "Produtos";
    public static final String TITULO_TOOLBAR_CLIENTES = "Clientes";
    public static final String TITULO_TOOLBAR_OPCOES = "Opções";

    //Atributos da tela
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private Menu menuDaTela = null;

//    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(TITULO_TOOLBAR_PEDIDOS);
        setSupportActionBar(toolbar);

        carregaFragmento(new PedidosFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        menuDaTela = menu;
        setVisibilidadeMenu(MENU_NOVO_PEDIDO);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_novo_pedido: {
                intent = new Intent(DashboardActivity.this, CadastroPedidoActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_novo_cliente: {
                intent = new Intent(DashboardActivity.this, CadastroClienteActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_novo_produto: {
                intent = new Intent(DashboardActivity.this, CadastroProdutoActivity.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {

            case R.id.menu_pedidos: {
                fragment = new PedidosFragment();
                toolbar.setTitle(TITULO_TOOLBAR_PEDIDOS);
                setVisibilidadeMenu(MENU_NOVO_PEDIDO);
                break;
            }
            case R.id.menu_pedidos_finalizados: {
                fragment = new PedidosFinalizadosFragment();
                toolbar.setTitle(TITULO_TOOLBAR_PEDIDOS_FINALIZADOS);
                setVisibilidadeMenu(MENU_PEDIDOS_FINALIZADOS);
                break;
            }
            case R.id.menu_produtos: {
                fragment = new ProdutosFragment();
                toolbar.setTitle(TITULO_TOOLBAR_PRODUTOS);
                setVisibilidadeMenu(MENU_NOVO_PRODUTO);
                break;
            }
            case R.id.menu_clientes: {
                fragment = new ClientesFragment();
                toolbar.setTitle(TITULO_TOOLBAR_CLIENTES);
                setVisibilidadeMenu(MENU_NOVO_CLIENTE);
                break;
            }
            case R.id.menu_opcoes: {
                fragment = new OpcoesFragment();
                toolbar.setTitle(TITULO_TOOLBAR_OPCOES);
                setVisibilidadeMenu(MENU_OPCOES);
                break;
            }
        }
        return carregaFragmento(fragment);
    }

    private boolean carregaFragmento(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rl_dashboard_container_fragments, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void setVisibilidadeMenu(int position) {

        switch (position) {
            case MENU_NOVO_PEDIDO: {
                menuDaTela.findItem(R.id.menu_novo_pedido).setVisible(true);
                menuDaTela.findItem(R.id.menu_novo_produto).setVisible(false);
                menuDaTela.findItem(R.id.menu_novo_cliente).setVisible(false);
                break;
            }
            case MENU_PEDIDOS_FINALIZADOS:
            case MENU_OPCOES: {
                menuDaTela.findItem(R.id.menu_novo_pedido).setVisible(false);
                menuDaTela.findItem(R.id.menu_novo_produto).setVisible(false);
                menuDaTela.findItem(R.id.menu_novo_cliente).setVisible(false);
                break;
            }
            case MENU_NOVO_PRODUTO: {
                menuDaTela.findItem(R.id.menu_novo_pedido).setVisible(false);
                menuDaTela.findItem(R.id.menu_novo_produto).setVisible(true);
                menuDaTela.findItem(R.id.menu_novo_cliente).setVisible(false);
                break;
            }
            case MENU_NOVO_CLIENTE: {
                menuDaTela.findItem(R.id.menu_novo_pedido).setVisible(false);
                menuDaTela.findItem(R.id.menu_novo_produto).setVisible(false);
                menuDaTela.findItem(R.id.menu_novo_cliente).setVisible(true);
                break;
            }
        }
    }
}
