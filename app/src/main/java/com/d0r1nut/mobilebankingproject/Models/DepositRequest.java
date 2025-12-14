package com.d0r1nut.mobilebankingproject.Models;

public class DepositRequest {
    private final double amount;
    private final String message;
    private final String email;

    public DepositRequest(double amount, String message, String email) {
        this.amount = amount;
        this.message = message;
        this.email = email;
    }
}