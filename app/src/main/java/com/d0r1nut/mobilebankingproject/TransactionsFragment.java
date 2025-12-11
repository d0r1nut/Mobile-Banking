package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.d0r1nut.mobilebankingproject.Adapters.TransactionAdapter;
import com.d0r1nut.mobilebankingproject.Models.Transaction;
import com.d0r1nut.mobilebankingproject.Services.FirebaseService;

import java.util.Collections;
import java.util.List;

public class TransactionsFragment extends Fragment {

    private RecyclerView rvTransactions;
    private TransactionAdapter transactionAdapter;
    private FirebaseService fbDatabase;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        rvTransactions = view.findViewById(R.id.rv_transactions);
        fbDatabase = new FirebaseService();

        if (getArguments() != null) {
            userId = getArguments().getString("USER_ID");
        }

        if (userId != null) {
            fbDatabase.getTransactionForUser(userId, new FirebaseService.OnTransactionsReceivedListener() {
                @Override
                public void onTransactionsReceived(List<Transaction> transactions) {
                    if (transactionAdapter == null) {
                        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
                        transactionAdapter = new TransactionAdapter(transactions, transaction -> {
                            Intent intent = new Intent(getActivity(), TransactionDetailActivity.class);
                            intent.putExtra("TRANSACTION_ID", transaction.getTransactionId());
                            startActivity(intent);
                        });
                        rvTransactions.setAdapter(transactionAdapter);
                        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
                    } else {
                        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
                        transactionAdapter.updateTransaction(transactions);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), "Error loading transactions!", Toast.LENGTH_LONG).show();
                }
            });
        }

        return view;
    }
}
//            fbDatabase.getTransactionForUser(userId, new FirebaseService.OnTransactionsReceivedListener() {
//                @Override
//                public void onTransactionsReceived(List<Transaction> transactions) {
//                    List<Transaction> scheduledTransactions = transactions.stream()
//                            .filter(t -> t.getTransactionStatus() == TransactionStatus.Scheduled)
//                            .collect(Collectors.toList());
//
//                    if (scheduledTransactionsAdapter == null) {
//                        scheduledTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
//
//                        scheduledTransactionsAdapter = new TransactionAdapter(scheduledTransactions, transaction -> {
//                            Intent intent = new Intent(HomeActivity.this, TransactionDetailActivity.class);
//                            intent.putExtra("USER_ID", transaction.getSenderId());
//                            startActivity(intent);
//                        });
//                        rvScheduledTransactions.setAdapter(scheduledTransactionsAdapter);
//                        rvScheduledTransactions.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
//                    }
//                    else {
//                        scheduledTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
//                        scheduledTransactionsAdapter.updateTransaction(scheduledTransactions);
//                    }
//
//                    List<Transaction> completedTransactions = transactions.stream()
//                            .filter(t -> t.getTransactionStatus() == TransactionStatus.Completed)
//                            .collect(Collectors.toList());
//
//                    if (completedTransactionsAdapter == null) {
//                        completedTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
//
//                        completedTransactionsAdapter = new TransactionAdapter(completedTransactions, transaction -> {
//                            Intent intent = new Intent(HomeActivity.this, TransactionDetailActivity.class);
//                            intent.putExtra("USER_ID", transaction.getSenderId());
//                            startActivity(intent);
//                        });
//                        rvCompletedTransactions.setAdapter(completedTransactionsAdapter);
//                        rvCompletedTransactions.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
//                    }
//                    else {
//                        completedTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
//                        completedTransactionsAdapter.updateTransaction(completedTransactions);
//                    }
//                }
//                @Override
//                public void onError(Exception e) {
//                    Toast.makeText(HomeActivity.this, "Error load transactions!", Toast.LENGTH_LONG).show();
//                }
//            });