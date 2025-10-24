package com.ulbra.bancodesenhas;

public class Banco {
    private String appName;
    private String login;
    private String senha;

    public Banco() {
        // Construtor vazio necessário para o Firebase
    }

    public Banco(String appName, String login, String senha) {
        this.appName = appName;
        this.login = login;
        this.senha = senha;
    }

    public String getAppName() {
        return appName;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }
}