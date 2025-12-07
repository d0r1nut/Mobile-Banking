package com.d0r1nut.mobilebankingproject.Models;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private List<Transaction> transactions;
    private static TransactionManager instance;
    private TransactionManager() {
        transactions = new ArrayList<>();
    }

    public static TransactionManager getInstance(){
        if(instance == null){
            instance = new TransactionManager();
        }
        return instance;
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    public List<Transaction> getTransactionsForUser(String transactionId){
        List<Transaction> userTransactions = new ArrayList<>();
        for(Transaction transaction: transactions){
            if(transaction.getTransactionId().equals(transactionId)){
                userTransactions.add(transaction);
            }
        }
        return userTransactions;
    }

    public void deleteTransaction(Transaction transaction){ transactions.remove(transaction); }

    public Transaction getTransactionById(String transactionId){
        for(Transaction transaction: transactions){
            if(transaction.getTransactionId().equals(transactionId)){
                return transaction;
            }
        }
        return null;
    }
}

