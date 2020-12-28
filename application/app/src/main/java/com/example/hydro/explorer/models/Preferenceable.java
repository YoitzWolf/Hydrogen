package com.example.hydro.explorer.models;

import android.util.Pair;

/*
class TokenPref extends Preference {
    private Token token;
    public final String STORAGE = "bc8c4c5c989caa38";

    public TokenPref(Token token){
        this.token = token;
    }

    public TokenPref(String token, TokenTypes type) throws IllegalAccessException {
        this.token = new Token(type, token);
    }

    public TokenPref(String token, String type) throws IllegalAccessException {
        this.token = new Token(TokenTypes.valueOf(type), token);
    }

    @Override
    public Pair<String, String> getPreference() {
        return new Pair<String, String>(this.token.getTokenType().name(), token.getToken());
    }
}
*/
public abstract class Preferenceable {

    protected Preferenceable() {
    }

    protected Preferenceable(String key, String value) {
    }

    public abstract Pair<String, String> getPreference();

    public abstract String getStorage();
}
