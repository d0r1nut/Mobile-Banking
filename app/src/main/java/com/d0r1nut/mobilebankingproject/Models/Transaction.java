package com.d0r1nut.mobilebankingproject.Models;

import java.util.Date;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private String senderId;

    private String recipientEmail;
    private Date date;
    private double amount;
    private String message;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private boolean acceptedByRecipient;

    public Transaction(String senderId, String recipientEmail, Date date, double amount, String message, TransactionType transactionType, TransactionStatus transactionStatus) {
        this.transactionId = UUID.randomUUID().toString();
        this.senderId = senderId;
        this.recipientEmail = recipientEmail;
        this.date = date;
        this.amount = amount;
        this.message = message;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.acceptedByRecipient = false;
    }
    public Transaction(String senderId, Date date, double amount, String message, TransactionType transactionType, TransactionStatus transactionStatus) {
        this.transactionId = UUID.randomUUID().toString();
        this.senderId = senderId;
        this.date = date;
        this.amount = amount;
        this.message = message;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.acceptedByRecipient = false;
    }
    public Transaction() {}

    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public String getRecipientEmail() {
        return recipientEmail;
    }
    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public TransactionType getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
    public boolean getAcceptedByRecipient() {
        return acceptedByRecipient;
    }
    public void setAcceptedByRecipient(boolean acceptedByRecipient) {
        this.acceptedByRecipient = acceptedByRecipient;
    }







}
