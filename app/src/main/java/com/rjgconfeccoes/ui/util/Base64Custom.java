package com.rjgconfeccoes.ui.util;

import android.util.Base64;

public class Base64Custom {

    public static String codificarStringBase64(String texto) {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("\\n|\\r", "");
    }

    public static String decodificarStringBase64(String textoCodificado) {
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
