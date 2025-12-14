package com.d0r1nut.mobilebankingproject.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.d0r1nut.mobilebankingproject.Models.Transaction;
import com.d0r1nut.mobilebankingproject.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseService {
    private final String TRANSACTION_NODE = "transactions";
    private final String USER_NODE = "users";
    private final DatabaseReference transactionDatabaseReference;
    private final DatabaseReference userDatabaseReference;


    public FirebaseService(){
        transactionDatabaseReference = FirebaseDatabase.getInstance().getReference(TRANSACTION_NODE);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference(USER_NODE);
    }

    public void addTransaction(Transaction transaction, Boolean isDeposit){
        transactionDatabaseReference.child(transaction.getTransactionId()).setValue(transaction);

        updateUserBalance(transaction.getSenderId(), transaction.getAmount(), isDeposit);
    }

    public void updateTransaction(Transaction transaction) {
        transactionDatabaseReference.child(transaction.getTransactionId()).setValue(transaction);
    }

    public void deleteTransaction(Transaction transaction){
        transactionDatabaseReference.child(transaction.getTransactionId()).removeValue();
    }

    public interface OnTransactionsReceivedListener{
        void onTransactionsReceived(List<Transaction> transactions);
        void onError(Exception e);
    }

    public interface OnTransactionReceivedListener{
        void onTransactionReceived(Transaction transaction);
        void onError(Exception e);
    }

    public interface OnBalanceChangedListener{
        void onBalanceChanged(double balance);
        void onError(Exception e);
    }

    public interface OnUserReceivedListener{
        void onUserReceived(User user);
        void onError(Exception e);
    }

    public void getTransactionForUser(String senderId, OnTransactionsReceivedListener listener){
        transactionDatabaseReference.orderByChild("senderId").equalTo(senderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Transaction> transactions = new ArrayList<>();
                        for(DataSnapshot transactionSnapshot: snapshot.getChildren()){
                            Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                            if(transaction != null){
                                transactions.add(transaction);
                            }
                        }
                        listener.onTransactionsReceived(transactions);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.toException());
                    }
                });
    }

    public void getTransactionsForRecipient(String recipientEmail, OnTransactionsReceivedListener listener) {
        transactionDatabaseReference.orderByChild("recipientEmail").equalTo(recipientEmail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Transaction> transactions = new ArrayList<>();
                        for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                            Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                            if (transaction != null) {
                                transactions.add(transaction);
                            }
                        }
                        listener.onTransactionsReceived(transactions);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.toException());
                    }
                });
    }

    public void getTransactionById(String transactionId, OnTransactionReceivedListener listener) {
        transactionDatabaseReference.child(transactionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Transaction transaction = snapshot.getValue(Transaction.class);
                    listener.onTransactionReceived(transaction);
                } else {
                    listener.onTransactionReceived(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.toException());
            }
        });
    }

    public void getBalanceChangedForUser(String userId, OnBalanceChangedListener listener) {
        userDatabaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        listener.onBalanceChanged(user.getBalance());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.toException());
            }
        });
    }

    public void getUserById(String userId, OnUserReceivedListener listener) {
        userDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    listener.onUserReceived(user);
                } else {
                    listener.onUserReceived(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.toException());
            }
        });
    }

    public void getUserByEmail(String email, OnUserReceivedListener listener) {
        userDatabaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        listener.onUserReceived(user);
                        return;
                    }
                }
                listener.onUserReceived(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.toException());
            }
        });
    }

    public void updateUserBalance(String userId, double amount, boolean isDeposit) {
        userDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        double currentBalance = user.getBalance();
                        double newBalance = isDeposit ? currentBalance + amount : currentBalance - amount;
                        snapshot.getRef().child("balance").setValue(newBalance);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Error updating balance for user: " + userId, error.toException());
            }
        });
    }
}