package com.example.hydro.request.models;

import android.util.Pair;

import com.example.hydro.explorer.models.Preferenceable;

public class Token extends Preferenceable {
    private TokenTypes type;
    public String token;

    public IllegalAccessException INVALID_TOKEN_ERR() {
        return new IllegalAccessException(String.format("INVALID TOKEN LENGTH NEED: {%d}, FOUND: {%d}", this.type.getSize(), this.token.length()));
    }

    public void setToken(byte token[]) throws IllegalAccessException {
        /*if(token.length != this.type.getSize()){
            throw new IllegalAccessException(String.format("INVALID TOKEN LENGTH NEED: {}, FOUND: {}", this.type.getSize(), token.length));
        }*/
        this.token = token.toString();
    }

    public void setToken(String token) throws IllegalAccessException {
        /*if(token.length() != this.type.getSize()){
            throw new IllegalAccessException(String.format("INVALID TOKEN LENGTH NEED: {}, FOUND: {}", this.type.getSize(), token.length));
        }*/
        this.token = token;
    }

    public Token() {
        this.token = "12345667890123451234566789012345";
        this.type = TokenTypes.Err;
    }

    public Token(TokenTypes type, String token) throws IllegalAccessException {
        this.type = type;
        this.token = token;
        if (token.length() != this.type.getSize()) {
            throw INVALID_TOKEN_ERR();
        }

    }

    public Token(String type, String token) throws IllegalAccessException {
        this.type = TokenTypes.valueOf(type);
        this.token = token;
        if (token.length() != this.type.getSize()) {
            throw INVALID_TOKEN_ERR();
        }
    }

    public Token(TokenTypes type, byte[] token) throws IllegalAccessException {
        this.type = type;
        this.token = token.toString();
        if (token.length != this.type.getSize()) {
            throw INVALID_TOKEN_ERR();
        }
    }


    public byte[] getTokenBytes() {
        return token.getBytes();
    }

    public String getToken() {
        return token;
    }

    public int getSize() {
        return this.type.getSize();
    }

    public TokenTypes getTokenType() {
        return this.type;
    }

    @Override
    public Pair<String, String> getPreference() {
        // key:value
        return new Pair<String, String>(this.getTokenType().name(), this.getToken());
    }

    @Override
    public String getStorage() {
        return "TOKENS";
    }

    public static String Storage() {
        return "TOKENS";
    }
}
