package com.d0r1nut.mobilebankingproject.Models;

import com.google.gson.annotations.SerializedName;

public class StatementWrapper {
    @SerializedName("statement")
    private TransactionHistoryResponse transactions;

    public TransactionHistoryResponse getTransactions() {
        return transactions;
    }
}
