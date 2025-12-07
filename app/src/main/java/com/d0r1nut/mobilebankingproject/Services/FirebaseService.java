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
    private String TRANSACTION_NODE = "transactions";
    private String USER_NODE = "users";
    private DatabaseReference transactionDatabaseReference;
    private DatabaseReference userDatabaseReference;


    public FirebaseService(){
        transactionDatabaseReference = FirebaseDatabase.getInstance().getReference(TRANSACTION_NODE);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference(USER_NODE);
    }

    public void addTransaction(Transaction transaction){
        transactionDatabaseReference.child(transaction.getTransactionId()).setValue(transaction);

        userDatabaseReference.child(transaction.getSenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        double currentBalance = user.getBalance();
                        double newBalance = 0;
                        if (Objects.equals(transaction.getTransactionType().toString(), "Deposit")) {
                            newBalance = currentBalance + transaction.getAmount();
                        } else {
                            newBalance = currentBalance - transaction.getAmount();
                        }
                        snapshot.getRef().child("balance").setValue(newBalance);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Error updating balance for user: " + transaction.getSenderId(), error.toException());
            }
        });
    }

    public void deleteTransaction(Transaction transaction){
        transactionDatabaseReference.child(transaction.getTransactionId()).removeValue();
    }


    public interface OnTransactionsReceivedListener{
        void onTransactionsReceived(List<Transaction> transactions);
        void onError(Exception e);
    }

    public interface OnBalanceChangedListener{
        void onBalanceChanged(double balance);
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
}
