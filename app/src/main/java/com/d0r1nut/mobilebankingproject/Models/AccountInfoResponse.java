package com.d0r1nut.mobilebankingproject.Models;

public class AccountInfoResponse {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String mobilePhone;

    public AccountInfoResponse(String firstName, String lastName, String email, String mobilePhone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobilePhone = mobilePhone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }
}