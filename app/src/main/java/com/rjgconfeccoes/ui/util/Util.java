package com.rjgconfeccoes.ui.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;
import com.rjgconfeccoes.R;

public abstract class Util {

    public static void mensagemDeAlerta(Context context, View view, String alerta) {
        Snackbar.make(view, alerta, Snackbar.LENGTH_LONG)
                .setBackgroundTint(context.getResources().getColor(R.color.corBranca)).show();
    }

    /**
     * Cria o progress bar
     */
    public static AlertDialog criaProgressBar(Context context)
    {
        AlertDialog.Builder builder = null;
        AlertDialog dlgProgressBar = null;
        LayoutInflater inflaterTela = null;
        View viewDialog = null;
        ProgressBar prgBar = null;
        TextView lblMensagem = null;

        //Instancia o dialog
        if (context != null)
        {
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

        //Esconde a mensagem
        lblMensagem.setVisibility(View.GONE);

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
    public static void escondeProgressBar(AlertDialog dlgAlerta)
    {
        //Verifica se o alertDialog é nulo
        if (dlgAlerta != null)
        {
            //Verifica se o alerta está mostrando
            if (dlgAlerta.isShowing())
            {
                //Da dismiss no alertDialog
                dlgAlerta.dismiss();
            }
        }
    }
}
