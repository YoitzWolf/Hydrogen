package com.example.hydro.request.models;

public class REGISTERBODY {
    public String login;
    public String email;
    public String password;

    public REGISTERBODY(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public boolean iscorrect() {
        if (login.length() > 0 && email.length() > 0 && password.length() > 0) {
            return true;
        }
        return false;
    }
}
