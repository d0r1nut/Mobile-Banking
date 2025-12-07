package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.d0r1nut.mobilebankingproject.Adapters.TransactionAdapter;
import com.d0r1nut.mobilebankingproject.Models.Transaction;
import com.d0r1nut.mobilebankingproject.Models.TransactionManager;
import com.d0r1nut.mobilebankingproject.Models.TransactionStatus;
import com.d0r1nut.mobilebankingproject.Services.FirebaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvScheduledTransactions, rvCompletedTransactions;
    private Button btnAddTransaction;
    TransactionAdapter scheduledTransactionsAdapter, completedTransactionsAdapter;
    private ArrayList<Transaction> currentTransactions;
    private FirebaseService fbDatabase;
    private TextView tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String userId = getIntent().getStringExtra("USER_ID");
        tvBalance = findViewById(R.id.tv_balance);
        rvScheduledTransactions = findViewById(R.id.rv_scheduled_transactions);
        rvCompletedTransactions = findViewById(R.id.rv_completed_transactions);
        btnAddTransaction = findViewById(R.id.btn_add_transaction);



        fbDatabase = new FirebaseService();

        fbDatabase.getTransactionForUser(userId, new FirebaseService.OnTransactionsReceivedListener() {
            @Override
            public void onTransactionsReceived(List<Transaction> transactions) {
                List<Transaction> scheduledTransactions = transactions.stream()
                        .filter(t -> t.getTransactionStatus() == TransactionStatus.Scheduled)
                        .collect(Collectors.toList());

                if (scheduledTransactionsAdapter == null) {
                    scheduledTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

                    scheduledTransactionsAdapter = new TransactionAdapter(scheduledTransactions, transaction -> {
                        Intent intent = new Intent(HomeActivity.this, TransactionDetailActivity.class);
                        intent.putExtra("USER_ID", transaction.getSenderId());
                        startActivity(intent);
                    });
                    rvScheduledTransactions.setAdapter(scheduledTransactionsAdapter);
                    rvScheduledTransactions.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                }
                else {
                    scheduledTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
                    scheduledTransactionsAdapter.updateTransaction(scheduledTransactions);
                }

                List<Transaction> completedTransactions = transactions.stream()
                        .filter(t -> t.getTransactionStatus() == TransactionStatus.Completed)
                        .collect(Collectors.toList());

                if (completedTransactionsAdapter == null) {
                    completedTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

                    completedTransactionsAdapter = new TransactionAdapter(completedTransactions, transaction -> {
                        Intent intent = new Intent(HomeActivity.this, TransactionDetailActivity.class);
                        intent.putExtra("USER_ID", transaction.getSenderId());
                        startActivity(intent);
                    });
                    rvCompletedTransactions.setAdapter(completedTransactionsAdapter);
                    rvCompletedTransactions.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                }
                else {
                    completedTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
                    completedTransactionsAdapter.updateTransaction(completedTransactions);
                }
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(HomeActivity.this, "Error load transactions!", Toast.LENGTH_LONG).show();
            }
        });
        fbDatabase.getBalanceChangedForUser(userId, new FirebaseService.OnBalanceChangedListener() {
            @Override
            public void onBalanceChanged(double balance) {
                tvBalance.setText(String.format(Locale.US, "%.2f", balance));
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(HomeActivity.this, "Error load balance!", Toast.LENGTH_LONG).show();
            }
        });

        btnAddTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddTransactionActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("BALANCE", tvBalance.getText());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}