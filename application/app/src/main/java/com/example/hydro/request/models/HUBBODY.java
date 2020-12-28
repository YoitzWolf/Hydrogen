package com.example.hydro.request.models;

public class HUBBODY {
    public String name;
    public String token; // Auth Token
    public String description;
    public boolean is_public;
    public int game_id;
    public String password;

    public HUBBODY(
            String name,
            String token,
            String description,
            int game_id,
            boolean is_public,
            String password
    ){
        this.name = name;
        this.description = description;
        this.token = token;
        this.is_public = is_public;
        this.password = password;
        this.game_id = game_id;
    }


}
