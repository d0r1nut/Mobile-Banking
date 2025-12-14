package com.d0r1nut.mobilebankingproject.Models;

public class TransferRequest {
    private final double amount;
    private final String message;
    private final String account;

    public TransferRequest(double amount, String message, String account) {
        this.amount = amount;
        this.message = message;
        this.account = account;
    }
}