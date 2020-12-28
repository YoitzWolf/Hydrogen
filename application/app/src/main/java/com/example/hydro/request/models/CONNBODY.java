package com.example.hydro.request.models;

public class CONNBODY {
    public int hub_id;
    public String token;
    public String password;

    public CONNBODY(int hub_id, String password, String token){
        this.hub_id = hub_id;
        this.password = password;
        this.token = token;
    }
}
