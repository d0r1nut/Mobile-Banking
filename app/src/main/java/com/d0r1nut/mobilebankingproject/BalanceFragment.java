package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.d0r1nut.mobilebankingproject.Adapters.TransactionAdapter;
import com.d0r1nut.mobilebankingproject.Models.Transaction;
import com.d0r1nut.mobilebankingproject.Models.TransactionStatus;
import com.d0r1nut.mobilebankingproject.Services.AuthFirebase;
import com.d0r1nut.mobilebankingproject.Services.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BalanceFragment extends Fragment {

    private TextView tvBalance;
    private RecyclerView rvScheduledTransactions, rvIncomingTransactions;
    private TransactionAdapter scheduledTransactionsAdapter, incomingTransactionsAdapter;
    private FloatingActionButton btnAddTransaction;
    private FirebaseService fbDatabase;
    private AuthFirebase fAuth;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, container, false);

        tvBalance = view.findViewById(R.id.tv_balance);
        rvScheduledTransactions = view.findViewById(R.id.rv_scheduled_transactions);
        rvIncomingTransactions = view.findViewById(R.id.rv_incoming_transactions);
        btnAddTransaction = view.findViewById(R.id.btn_add_transaction);
        fbDatabase = new FirebaseService();
        fAuth = new AuthFirebase();

        if (getArguments() != null) {
            userId = getArguments().getString("USER_ID");
        }

        if (userId != null) {
            loadBalance();
            loadScheduledTransactions();
            loadIncomingTransactions();
        }

        btnAddTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
            intent.putExtra("USER_ID", userId);
            String balance = tvBalance.getText().toString().replace("$", "");
            intent.putExtra("BALANCE", balance);
            startActivity(intent);
        });

        return view;
    }

    private void loadBalance() {
        fbDatabase.getBalanceChangedForUser(userId, new FirebaseService.OnBalanceChangedListener() {
            @Override
            public void onBalanceChanged(double balance) {
                tvBalance.setText(String.format(Locale.US, "$%.2f", balance));
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Error loading balance!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadScheduledTransactions() {
        fbDatabase.getTransactionForUser(userId, new FirebaseService.OnTransactionsReceivedListener() {
            @Override
            public void onTransactionsReceived(List<Transaction> transactions) {
                List<Transaction> scheduledTransactions = transactions.stream()
                        .filter(t -> t.getTransactionStatus() == TransactionStatus.Scheduled)
                        .collect(Collectors.toList());

                if (scheduledTransactionsAdapter == null) {
                    scheduledTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
                    scheduledTransactionsAdapter = new TransactionAdapter(scheduledTransactions, transaction -> {
                        Intent intent = new Intent(getActivity(), TransactionDetailActivity.class);
                        intent.putExtra("TRANSACTION_ID", transaction.getTransactionId());
                        startActivity(intent);
                    });
                    rvScheduledTransactions.setAdapter(scheduledTransactionsAdapter);
                    rvScheduledTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    scheduledTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
                    scheduledTransactionsAdapter.updateTransaction(scheduledTransactions);
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Error loading transactions!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadIncomingTransactions() {
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null) {
                fbDatabase.getTransactionsForRecipient(email, new FirebaseService.OnTransactionsReceivedListener() {
                    @Override
                    public void onTransactionsReceived(List<Transaction> transactions) {
                        List<Transaction> incomingTransactions = transactions.stream()
                                .filter(t -> t.getTransactionStatus() == TransactionStatus.Scheduled)
                                .collect(Collectors.toList());

                        if (incomingTransactionsAdapter == null) {
                            incomingTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
                            incomingTransactionsAdapter = new TransactionAdapter(incomingTransactions, transaction -> {
                                Intent intent = new Intent(getActivity(), TransactionDetailActivity.class);
                                intent.putExtra("TRANSACTION_ID", transaction.getTransactionId());
                                startActivity(intent);
                            });
                            rvIncomingTransactions.setAdapter(incomingTransactionsAdapter);
                            rvIncomingTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
                        } else {
                            incomingTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
                            incomingTransactionsAdapter.updateTransaction(incomingTransactions);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "Error loading incoming transactions!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}