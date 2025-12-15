package com.d0r1nut.mobilebankingproject.Models;

import java.util.List;

public class TransactionHistoryResponse {
    private final List<TransactionItem> transactions;

    public TransactionHistoryResponse(List<TransactionItem> transactions) {
        this.transactions = transactions;
    }

    public List<TransactionItem> getTransactions() {
        return transactions;
    }

    public static class TransactionItem {
        private final double amount;
        private final String date;
        private final TransactionAccount fromAccount;
        private final String message;
        private final TransactionAccount toAccount;
        private final String transactionId;
        private final String type;

        public TransactionItem(double amount, String date, TransactionAccount fromAccount, String message, TransactionAccount toAccount, String transactionId, String type) {
            this.amount = amount;
            this.date = date;
            this.fromAccount = fromAccount;
            this.message = message;
            this.toAccount = toAccount;
            this.transactionId = transactionId;
            this.type = type;
        }

        public double getAmount() {
            return amount;
        }
        public String getDate() {
            return date;
        }
        public TransactionAccount getFromAccount() { return fromAccount; }
        public String getMessage() { return message; }
        public TransactionAccount getToAccount() { return toAccount; }
        public String getTransactionId() { return transactionId; }
        public String getType() {
            return type;
        }
    }

    public static class TransactionAccount {
        private final String accountId;
        private final String email;
        private final String firstName;
        private final String lastName;
        private final String mobilePhone;

        public TransactionAccount(String accountId, String email, String firstName, String lastName, String mobilePhone) {
            this.accountId = accountId;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.mobilePhone = mobilePhone;
        }

        public String getAccountId() { return accountId; }
        public String getEmail() { return email; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getMobilePhone() { return mobilePhone; }
    }
}