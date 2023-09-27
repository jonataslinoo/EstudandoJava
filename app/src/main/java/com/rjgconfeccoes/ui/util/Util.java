package com.rjgconfeccoes.ui.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;
import com.rjgconfeccoes.R;
import com.rjgconfeccoes.model.Dados;

import java.util.Calendar;

public abstract class Util {

    //Variáveis do apoio
    private static Dados dados = null;
    public static String USUARIOS = "usuarios";
    public static String CLIENTES = "clientes";
    public static String PRODUTOS = "produtos";
    public static String PEDIDOS = "pedidos";
    public static String PEDIDOS_FINALIZADOS = "pedidos_finalizados";
    public static String SELECIONE_CLIENTE = "Selecione um cliente";

    /**
     * Retorna a instancia da InfoControle a ativa no sistema
     */
    public static Dados recuperaDados() {
        if (dados == null) {
            dados = new Dados();
        }
        return dados;
    }

    /**
     * Define a variável de Dados
     */
    public static void defineDados(Dados dadosParam) {
        dados = dadosParam;
    }

    public static void mensagemDeAlerta(Context context, View view, String alerta) {
        Snackbar.make(view, alerta, Snackbar.LENGTH_LONG)
                .setBackgroundTint(context.getResources().getColor(R.color.corBranca)).show();
    }

    /**
     * Transforma data int para exibição
     * Retorna a data do pedido no formato dd/mm/yyyy hh:mm:ss
     */
    public static String converteDataHorasSegundos(long lData) {
        String sData = String.valueOf(lData);

        if (sData.length() != 14) {
            return "";
        }

        return String.format("%1$s/%2$s/%3$s %4$s:%5$s:%6$s", sData.substring(6, 8), sData.substring(4, 6), sData.substring(0, 4), sData.substring(8, 10), sData.substring(10, 12), sData.substring(12, 14));
    }

    public static String formataPreco(double dValor) {
        String sValor = String.format("%.2f", dValor);

        return sValor;
    }

    /**
     * Retorna a data do Sistema no formato yyyyMMddkkmmss
     */
    public static long obtemDataAtualComHoraSegundo() {
        return Long.parseLong(DateFormat.format("yyyyMMddkkmmss", Calendar.getInstance()).toString());
    }

    /**
     * Cria o progress bar
     */
    public static AlertDialog criaProgressBar(Context context, String mensagem) {
        AlertDialog.Builder builder = null;
        AlertDialog dlgProgressBar = null;
        LayoutInflater inflaterTela = null;
        View viewDialog = null;
        ProgressBar prgBar = null;
        TextView lblMensagem = null;

        //Instancia o dialog
        if (context != null) {
            builder = new AlertDialog.Builder(context);
        }

        // Inflamos o layout do teclado
        inflaterTela = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewDialog = inflaterTela.inflate(R.layout.dlg_progress_bar, null);

        //seta a view
        builder.setView(viewDialog);

        //Pega o progressBar
        prgBar = viewDialog.findViewById(R.id.progressBar);
        lblMensagem = viewDialog.findViewById(R.id.lblMensagemDialog);

        if (mensagem.isEmpty()) {
            //Esconde a mensagem
            lblMensagem.setVisibility(View.GONE);
        } else {
            //mostra a mensagem enviada
            lblMensagem.setVisibility(View.VISIBLE);
            lblMensagem.setText(mensagem);
        }

        //Coloca a cor branco no progressbar
        prgBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.corBranca), PorterDuff.Mode.SRC_IN);

        //Cria o dialogo
        dlgProgressBar = builder.create();
        dlgProgressBar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlgProgressBar.setCancelable(false);
        dlgProgressBar.show();

        return dlgProgressBar;
    }

    /**
     * Some o alertDialog com progressBar
     */
    public static void escondeProgressBar(AlertDialog dlgAlerta) {
        //Verifica se o alertDialog é nulo
        if (dlgAlerta != null) {
            //Verifica se o alerta está mostrando
            if (dlgAlerta.isShowing()) {
                //Da dismiss no alertDialog
                dlgAlerta.dismiss();
            }
        }
    }
}
