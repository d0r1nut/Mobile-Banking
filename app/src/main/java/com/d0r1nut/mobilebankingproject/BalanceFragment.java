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
import com.d0r1nut.mobilebankingproject.Services.FirebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BalanceFragment extends Fragment {

    private TextView tvBalance;
    private RecyclerView rvScheduledTransactions;
    private TransactionAdapter scheduledTransactionsAdapter;
    private FloatingActionButton btnAddTransaction;
    private FirebaseService fbDatabase;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, container, false);

        tvBalance = view.findViewById(R.id.tv_balance);
        rvScheduledTransactions = view.findViewById(R.id.rv_scheduled_transactions);
        btnAddTransaction = view.findViewById(R.id.btn_add_transaction);
        fbDatabase = new FirebaseService();

        if (getArguments() != null) {
            userId = getArguments().getString("USER_ID");
        }

        if (userId != null) {
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

        btnAddTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("BALANCE", tvBalance.getText().toString().replace("$", ""));
            startActivity(intent);
        });

        return view;
    }
}