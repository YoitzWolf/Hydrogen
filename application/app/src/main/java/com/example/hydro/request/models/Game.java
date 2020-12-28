package com.example.hydro.request.models;

public class Game {
    public int id; // : self.id
    public String name; // : self.name
    public String owner; // : self.owner.login
    public String description; // : self.description

    public Game(int id, String name, String owner, String description){
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
    }

    public String toString(){
        return  this.name;
    }

    public String getName() {
        return  this.name;
    }
}
