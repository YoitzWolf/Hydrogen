package com.example.hydro.request.models;

public enum TokenTypes {
    Auth(32), Refr(32), Err(32), Ok(32);

    private int size;

    TokenTypes(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

}
