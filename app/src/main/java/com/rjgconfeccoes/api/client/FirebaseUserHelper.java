package com.rjgconfeccoes.api.client;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.rjgconfeccoes.config.ConfiguracaoFirebase;
import com.rjgconfeccoes.model.Usuario;

public class FirebaseUserHelper {

    public static <T> void cadastraUsuario(Usuario usuario, OnDataFetchedListener<T> listener) {

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        //Recupero dados usuario cadastrado
//                        String identificadorUsuario = Base64Custom.codificarStringBase64(usuario.getEmail());

//                        usuario.setId(identificadorUsuario);
//                        usuario.salvar();
//                        limpaCampos();
//                        deslogaUsuarioEEfetuaLogin();

                        listener.onDataFetched((T) "Sucesso ao cadastrar usuario!");

                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException ex) {
                            listener.onCancelled(new Exception("A senha deve ter o mínimo de 6 caracteres!"));
                        } catch (FirebaseAuthInvalidCredentialsException ex) {
                            listener.onCancelled(new Exception("O e-mail digitado é invalido. Digite outro e-mail."));
                        } catch (FirebaseAuthUserCollisionException ex) {
                            listener.onCancelled(new Exception("Esse e-mail já está em uso. Utilize outro email."));
                        } catch (Exception ex) {
                            listener.onCancelled(new Exception("Erro ao cadastrar usuario!"));
                        }
                    }
                });
    }

    /**
     * Desloga o usuario cadastrado que o sistema loga sozinho
     * e efetua o login do usuario que ja estava logado anteriormente
     */
    private void deslogaUsuarioEEfetuaLogin() {
//        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
//        autenticacao.signOut();
//
//        //recupero dados do usuario logado salvo nas preferencias
//        Preferencias preferencias = new Preferencias(this);
//        HashMap<String, String> usuarioLogado = preferencias.getDadosUsuarioLogado();
//        String emailSalvo = usuarioLogado.get(CHAVE_EMAIL_PREFERENCIAS);
//        String senhaSalva = usuarioLogado.get(CHAVE_SENHA_PREFERENCIAS);
//
//        autenticacao.signInWithEmailAndPassword(emailSalvo, senhaSalva).addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Util.mensagemDeAlerta(CadastroUsuarioActivity.this, constraintLayout, getString(R.string.msg_erro_conexao_efetue_login_novamente));
//            }
//            vaiParaLogin();
//        });
    }

}
