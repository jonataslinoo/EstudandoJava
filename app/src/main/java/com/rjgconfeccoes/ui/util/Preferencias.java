package com.rjgconfeccoes.ui.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import static com.rjgconfeccoes.ui.Const.Constantes.CHAVE_EMAIL_PREFERENCIAS;
import static com.rjgconfeccoes.ui.Const.Constantes.CHAVE_NOME_PREFERENCIAS;
import static com.rjgconfeccoes.ui.Const.Constantes.CHAVE_PODE_CADASTRAR_PREFERENCIAS;
import static com.rjgconfeccoes.ui.Const.Constantes.CHAVE_SENHA_PREFERENCIAS;

public class Preferencias {

    private Context context;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "identificador.Preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    public Preferencias(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDadosUsuario(String nomeUsuarioLogado, String emailUsuarioLogado, String senhaUsuarioLogado, boolean permitirCadastro) {
        editor.putString(CHAVE_NOME_PREFERENCIAS, nomeUsuarioLogado);
        editor.putString(CHAVE_EMAIL_PREFERENCIAS, emailUsuarioLogado);
        editor.putString(CHAVE_SENHA_PREFERENCIAS, senhaUsuarioLogado);
        editor.putBoolean(CHAVE_PODE_CADASTRAR_PREFERENCIAS, permitirCadastro);
        editor.commit();
    }

    public HashMap<String, String> getDadosUsuarioLogado() {

        HashMap<String, String> dadosUsuario = new HashMap<>();

        dadosUsuario.put(CHAVE_EMAIL_PREFERENCIAS, preferences.getString(CHAVE_EMAIL_PREFERENCIAS, null));
        dadosUsuario.put(CHAVE_SENHA_PREFERENCIAS, preferences.getString(CHAVE_SENHA_PREFERENCIAS, null));

        return dadosUsuario;
    }

    public boolean getUsuarioLogadoPodeCadastrar() {
        return preferences.getBoolean(CHAVE_PODE_CADASTRAR_PREFERENCIAS, false);
    }

    public String getNomeUsuarioLogado() {
        String nomeUsuarioLogado = "";
        return preferences.getString(CHAVE_NOME_PREFERENCIAS, nomeUsuarioLogado);
    }
}
