package com.d0r1nut.mobilebankingproject.Models;

public class ActivateAccountRequest {
    private final String email;
    private final String activationCode;

    public ActivateAccountRequest(String email, String activationCode) {
        this.email = email;
        this.activationCode = activationCode;
    }
}