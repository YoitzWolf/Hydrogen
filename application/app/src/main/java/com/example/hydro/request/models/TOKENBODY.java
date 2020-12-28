package com.example.hydro.request.models;

import android.util.Pair;

public class TOKENBODY {
    public String token;
    public String token_type;
    public int message;

    public TOKENBODY(String token, String token_type){
        this.token = token;
        this.token_type = token_type;
        this.message = 0;
    }

    public TOKENBODY(String token, String token_type, int message){
        this.token = token;
        this.token_type = token_type;
        this.message = message;
    }

    public TOKENBODY(Pair<String, String> preference) {
        this.token_type = preference.first;
        this.token = preference.second;
        this.message = 0;
    }

    @Override
    public String toString() {
        return "TOKENBODY{" +
                "token='" + token + '\'' +
                ", token_type='" + token_type + '\'' +
                '}';
    }

    public Token getToken() throws IllegalAccessException {
        return new Token(TokenTypes.valueOf(token_type), this.token);
    }

    public String getMessage() {
        return Integer.toString(this.message);
    }
}
