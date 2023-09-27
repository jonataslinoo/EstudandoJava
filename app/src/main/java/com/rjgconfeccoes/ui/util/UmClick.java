package com.rjgconfeccoes.ui.util;

import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UmClick implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    //Variaveis da classe
    private View.OnClickListener listener = null;
    private BottomNavigationView.OnNavigationItemSelectedListener bottomListener = null;
    private long tempoUltimoCLique = 0;

    //Construtor da classe
    public UmClick(View.OnClickListener listener) {
        this.listener = listener;
    }

    public UmClick(BottomNavigationView.OnNavigationItemSelectedListener bottomListener) {
        this.bottomListener = bottomListener;
    }

    /**
     * O UmClick foi criado pois a ação dos botões estavam sendo acionadas duas vezes seguidas
     * abrindo posteriormente do clique duplo seguido dois dialogos da senha
     * substituindo o 'this' no 'setOnClickListener' por 'UmClick' há um intervalo de tempo
     * para que não ocorra dois eventos após o clique duplo seguido
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        //Se o tempo real menos o tempo do ultimo clique for menor que 1 segundo
        if (SystemClock.elapsedRealtime() - tempoUltimoCLique < 1000) {
            //Retorna
            return;
        }

        //Obtem o tempo real do aplicativo
        tempoUltimoCLique = SystemClock.elapsedRealtime();

        //Faz o clique
        listener.onClick(view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Se o tempo real menos o tempo do ultimo clique for menor que 0.5 decimos de segundo
        if (SystemClock.elapsedRealtime() - tempoUltimoCLique < 500) {
            //Retorna
            return false;
        }

        //Obtem o tempo real do aplicativo
        tempoUltimoCLique = SystemClock.elapsedRealtime();

        //Faz o clique
        return bottomListener.onNavigationItemSelected(item);
    }
}