package com.rjgconfeccoes.model;

import android.util.Patterns;

public class Usuario {

    private String nome;
    private String cpf;
    private Cargo cargo;
    private String email;
    private String senha;

    public Usuario(String nome, String cpf, Cargo cargo, String email, String senha) throws Exception {

        if (!validarCPF(cpf)) {
            throw new RuntimeException("O CPF é inválido!");
        }

        if (!validarEmail(email)) {
            throw new RuntimeException("O Email é inválido");
        }

        if (!validarSenha(senha)) {
            throw new RuntimeException("A senha não pode ser menor que 6 digitos");
        }

        this.nome = nome;
        this.cpf = cpf;
        this.cargo = cargo;
        this.email = email;
        this.senha = senha;
    }

//    public void salvar() {
////        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
////        databaseReference.child(Util.USUARIOS).child(getId()).setValue(this);
//    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    private boolean validarEmail(String email) {
        return email.matches(String.valueOf(Patterns.EMAIL_ADDRESS));
    }

    private boolean validarSenha(String senha) {
        return senha.length() >= 6;
    }

    public static boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int primeiroDigitoVerificador = 11 - (soma % 11);

        if (primeiroDigitoVerificador == 10 || primeiroDigitoVerificador == 11) {
            primeiroDigitoVerificador = 0;
        }

        if (primeiroDigitoVerificador != (cpf.charAt(9) - '0')) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        int segundoDigitoVerificador = 11 - (soma % 11);

        if (segundoDigitoVerificador == 10 || segundoDigitoVerificador == 11) {
            segundoDigitoVerificador = 0;
        }

        return segundoDigitoVerificador == (cpf.charAt(10) - '0');
    }
}
