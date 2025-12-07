package com.d0r1nut.mobilebankingproject.Models;

public class User {
    private String userId;
    private String email;
    private double balance;

    public User(String userId, String email, double balance) {
        this.userId = userId;
        this.email = email;
        this.balance = balance;
    }
    public User(String userId, String email) {
        this.userId = userId;
        this.email = email;
        this.balance = 0;
    }
    public User(){}
    public void deposit(double amount) {
        balance += amount;
    }
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        }
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

}
