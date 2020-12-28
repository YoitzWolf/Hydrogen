package com.example.hydro.request.models;

public class Connection {
    public int id; // : self.id
    public int owner_id;
    public Hub hub;

    public Connection (int id, int owner_id, Hub hub) {
        this.id = id;
        this.owner_id = owner_id;
        this.hub = hub;
    }
}
