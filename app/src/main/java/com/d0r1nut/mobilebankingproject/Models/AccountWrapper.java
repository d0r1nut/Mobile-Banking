package com.d0r1nut.mobilebankingproject.Models;

import com.google.gson.annotations.SerializedName;

public class AccountWrapper {
    @SerializedName("account")
    private AccountInfoResponse account;

    public AccountInfoResponse getAccount() {
        return account;
    }
}
