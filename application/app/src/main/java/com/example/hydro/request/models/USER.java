package com.example.hydro.request.models;

public class USER {
    public String login;
    public String email;
    public String avatar;
    public int avatar_size;
    public String status;

    public USER() {
        this.login = "None";
        this.email = "None";
        this.avatar = "None";
        this.status = "None";
        this.avatar_size = 0;
    }

    public USER(String login, String email, String avatar, String status) {
        this.login = login;
        this.email = email;
        this.avatar = avatar;
        this.status = status;
        this.avatar_size = 0;
    }


}
