package com.example.hydro.request.models;

// Simple body request templated class
public class SIMPLEREQUEST<ELEMENT> {
    public ELEMENT body;

    public ELEMENT getBody() {
        return body;
    }

    public void setBody(ELEMENT body) {
        this.body = body;
    }
}
