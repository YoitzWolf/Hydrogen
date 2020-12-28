package com.example.hydro.request.models;

public class LOGINBODY {
    public String login;
    public String password;

    public LOGINBODY(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public boolean iscorrect() {
        if (login.length() > 0 && password.length() > 0) {
            return true;
        }
        return false;
    }
}
