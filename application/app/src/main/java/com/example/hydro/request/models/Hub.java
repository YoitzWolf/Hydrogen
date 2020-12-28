package com.example.hydro.request.models;

public class Hub {

    public int id; // : self.id
    public String name; // : self.name
    public String owner; // : self.owner.login
    public String description; // : self.description
    public Game game; // : self.game.get_json()
    public boolean is_public; //: self.public
    public int max_players; // : self.users_limit
    public int players; // : len(self.connections),

    public Hub(String name){
        this.name = name;
    }

    public Hub(int id,
               String name,
               String owner,
               String description,
               Game game,
               boolean is_public,
               int max_players,
               int players){

        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.game = game;
        this.is_public = is_public;
        this.max_players = max_players;
        this.players = players;
    }

    public String getName() {
        return this.name;
    }
}
