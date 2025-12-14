package com.d0r1nut.mobilebankingproject.Models;

public class CreateAccountRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String mobilePhone;
    private final String password;

    public CreateAccountRequest(String firstName, String lastName, String email, String mobilePhone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.password = password;
    }
}