package com.d0r1nut.mobilebankingproject.Models;

public class BalanceResponse {
    private final double balance;

    public BalanceResponse(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
}