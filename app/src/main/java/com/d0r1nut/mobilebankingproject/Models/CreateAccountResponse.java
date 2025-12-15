package com.d0r1nut.mobilebankingproject.Models;

public class CreateAccountResponse {
    private String uid;

    public CreateAccountResponse(String uid) {
        this.uid = uid;
    }

    public CreateAccountResponse() { }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}