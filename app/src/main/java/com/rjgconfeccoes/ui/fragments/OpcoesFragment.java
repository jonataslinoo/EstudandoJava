package com.rjgconfeccoes.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.rjgconfeccoes.R;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.ui.activity.CadastroUsuarioActivity;
import com.rjgconfeccoes.ui.activity.LoginActivity;
import com.rjgconfeccoes.ui.util.Preferencias;

public class OpcoesFragment extends Fragment {

    private Context context;
    private Button botao_novo_usuario;
    private Button botao_sair;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retornaUsuario();
        configuraBotaoSair();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_opcoes, container, false);
        context = container.getContext();
        botao_novo_usuario = view.findViewById(R.id.fragment_opcoes_botao_novo_usuario);
        botao_sair = view.findViewById(R.id.fragment_opcoes_botao_sair);

        return view;
    }

    private void configuraBotaoSair() {
        botao_sair.setOnClickListener(v -> confirmaSair());
    }

    private void retornaUsuario() {
        //Recupera instancia Firebase do usuario logado
        Preferencias preferencias = new Preferencias(context);
        boolean usuarioLogadoPodeCadastrar = preferencias.getUsuarioLogadoPodeCadastrar();

        verificaUsuarioPodeCadastrar(usuarioLogadoPodeCadastrar);
    }

    private void verificaUsuarioPodeCadastrar(boolean podeCadastrarUsuario) {
        if (podeCadastrarUsuario) {
            botao_novo_usuario.setVisibility(View.VISIBLE);
            botao_novo_usuario.setEnabled(true);
            configuraBotaoNovoUsuario();
        } else {
            botao_novo_usuario.setVisibility(View.GONE);
            botao_novo_usuario.setEnabled(false);
        }
    }

    private void configuraBotaoNovoUsuario() {
        botao_novo_usuario.setOnClickListener(v -> vaiParaCadastroUsuario());
    }

    private void vaiParaCadastroUsuario() {
        Intent intent = new Intent(context, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    public void confirmaSair() {
        new AlertDialog
                .Builder(context)
                .setTitle("Atenção")
                .setMessage("Você sairá da sua conta, deseja continuar?")
                .setPositiveButton("Sim", (dialogInterface, i) -> vaiParaTelaLoginESaiDaConta())
                .setNegativeButton("Não", null)
                .show();
    }

    private void vaiParaTelaLoginESaiDaConta() {
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signOut();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }
}