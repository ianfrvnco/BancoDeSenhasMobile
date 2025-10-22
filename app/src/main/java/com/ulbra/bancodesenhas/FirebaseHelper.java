package com.ulbra.bancodesenhas;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitária para gerenciar operações do Firebase
 * Centraliza toda a lógica de autenticação e banco de dados
 */
public class FirebaseHelper {

    private static final String TAG = "FirebaseHelper";
    private static final String COLLECTION_USUARIOS = "usuarios";

    private static FirebaseAuth auth;
    private static FirebaseFirestore db;

    /**
     * Obtém a instância do Firebase Authentication
     * Padrão Singleton para garantir uma única instância
     */
    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
            Log.d(TAG, "Firebase Auth inicializado");
        }
        return auth;
    }

    /**
     * Obtém a instância do Firestore Database
     * Padrão Singleton para garantir uma única instância
     */
    public static FirebaseFirestore getFirestore() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
            Log.d(TAG, "Firestore inicializado");
        }
        return db;
    }

    /**
     * Verifica se existe um usuário logado no momento
     * @return true se houver usuário logado, false caso contrário
     */
    public static boolean isUserLoggedIn() {
        boolean logado = getAuth().getCurrentUser() != null;
        Log.d(TAG, "Usuário logado: " + logado);
        return logado;
    }

    /**
     * Obtém o usuário atualmente logado
     * @return FirebaseUser ou null se não houver usuário logado
     */
    public static FirebaseUser getCurrentUser() {
        FirebaseUser user = getAuth().getCurrentUser();
        if (user != null) {
            Log.d(TAG, "Usuário atual: " + user.getEmail());
        }
        return user;
    }

    /**
     * Cadastra um novo usuário no Firebase Authentication
     * e salva dados adicionais no Firestore
     *
     * @param email E-mail do usuário
     * @param senha Senha do usuário
     * @param nome Nome completo do usuário
     * @param successListener Callback executado em caso de sucesso
     * @param failureListener Callback executado em caso de erro
     */
    public static void cadastrarUsuario(String email, String senha, String nome,
                                        OnSuccessListener successListener,
                                        OnFailureListener failureListener) {
        Log.d(TAG, "Iniciando cadastro de usuário: " + email);

        getAuth().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Usuário criado com sucesso no Authentication");
                        FirebaseUser user = getAuth().getCurrentUser();

                        if (user != null) {
                            // Salvar dados adicionais no Firestore
                            salvarDadosUsuario(user.getUid(), nome, email, successListener, failureListener);
                        } else {
                            Log.e(TAG, "Erro: Usuário nulo após criação");
                            failureListener.onFailure("Erro ao obter dados do usuário");
                        }
                    } else {
                        // Tratar erros de cadastro
                        String erro = task.getException() != null ?
                                task.getException().getMessage() : "Erro desconhecido";
                        Log.e(TAG, "Erro no cadastro: " + erro);
                        failureListener.onFailure(erro);
                    }
                });
    }

    /**
     * Salva dados adicionais do usuário no Firestore
     * Cria um documento na coleção "usuarios" com o UID como ID
     *
     * @param uid ID único do usuário (vem do Authentication)
     * @param nome Nome completo do usuário
     * @param email E-mail do usuário
     * @param successListener Callback de sucesso
     * @param failureListener Callback de erro
     */
    private static void salvarDadosUsuario(String uid, String nome, String email,
                                           OnSuccessListener successListener,
                                           OnFailureListener failureListener) {
        Log.d(TAG, "Salvando dados do usuário no Firestore: " + uid);

        // Criar objeto com os dados do usuário
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nome", nome);
        usuario.put("email", email);
        usuario.put("dataCadastro", System.currentTimeMillis());
        usuario.put("ativo", true);

        // Salvar no Firestore
        getFirestore().collection(COLLECTION_USUARIOS)
                .document(uid)
                .set(usuario)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Dados salvos com sucesso no Firestore");
                    successListener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    String erro = e.getMessage() != null ? e.getMessage() : "Erro ao salvar dados";
                    Log.e(TAG, "Erro ao salvar no Firestore: " + erro);
                    failureListener.onFailure(erro);
                });
    }

    /**
     * Realiza o login do usuário com e-mail e senha
     *
     * @param email E-mail do usuário
     * @param senha Senha do usuário
     * @param successListener Callback executado em caso de sucesso
     * @param failureListener Callback executado em caso de erro
     */
    public static void fazerLogin(String email, String senha,
                                  OnSuccessListener successListener,
                                  OnFailureListener failureListener) {
        Log.d(TAG, "Tentando fazer login: " + email);

        getAuth().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Login realizado com sucesso");
                        FirebaseUser user = getAuth().getCurrentUser();
                        if (user != null) {
                            Log.d(TAG, "Usuário logado: " + user.getEmail());
                        }
                        successListener.onSuccess();
                    } else {
                        String erro = task.getException() != null ?
                                task.getException().getMessage() : "Erro desconhecido";
                        Log.e(TAG, "Erro no login: " + erro);
                        failureListener.onFailure(erro);
                    }
                });
    }

    /**
     * Obtém dados do usuário do Firestore
     *
     * @param uid ID do usuário
     * @param listener Callback com os dados do usuário
     */
    public static void obterDadosUsuario(String uid, OnDataListener listener) {
        Log.d(TAG, "Buscando dados do usuário: " + uid);

        getFirestore().collection(COLLECTION_USUARIOS)
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "Dados do usuário encontrados");
                        listener.onDataReceived(documentSnapshot.getData());
                    } else {
                        Log.w(TAG, "Documento do usuário não encontrado");
                        listener.onDataReceived(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao buscar dados: " + e.getMessage());
                    listener.onDataReceived(null);
                });
    }

    /**
     * Faz logout do usuário atual
     */
    public static void fazerLogout() {
        FirebaseUser user = getAuth().getCurrentUser();
        if (user != null) {
            Log.d(TAG, "Fazendo logout do usuário: " + user.getEmail());
        }
        getAuth().signOut();
        Log.d(TAG, "Logout realizado");
    }

    // ============= INTERFACES DE CALLBACK =============

    /**
     * Interface para callback de sucesso
     */
    public interface OnSuccessListener {
        void onSuccess();
    }

    /**
     * Interface para callback de erro
     */
    public interface OnFailureListener {
        void onFailure(String erro);
    }

    /**
     * Interface para callback de dados
     */
    public interface OnDataListener {
        void onDataReceived(Map<String, Object> data);
    }
}