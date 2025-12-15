package com.d0r1nut.mobilebankingproject.Models;

import com.google.gson.annotations.SerializedName;

public class AllAccountsWrapper {
    @SerializedName("accounts")
    private AccountInfoResponse accounts;

    public AccountInfoResponse getAllAccounts() {
        return accounts;
    }
}
