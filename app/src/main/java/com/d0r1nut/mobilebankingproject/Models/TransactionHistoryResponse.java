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
        private final String date;
        private final String description;
        private final double amount;
        private final String type;

        public TransactionItem(String date, String description, double amount, String type) {
            this.date = date;
            this.description = description;
            this.amount = amount;
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public double getAmount() {
            return amount;
        }

        public String getType() {
            return type;
        }
    }
}