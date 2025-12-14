package com.d0r1nut.mobilebankingproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.d0r1nut.mobilebankingproject.Models.Transaction;
import com.d0r1nut.mobilebankingproject.Models.TransactionStatus;
import com.d0r1nut.mobilebankingproject.Models.User;
import com.d0r1nut.mobilebankingproject.Services.AuthFirebase;
import com.d0r1nut.mobilebankingproject.Services.FirebaseService;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TransactionDetailActivity extends AppCompatActivity {

    private TextView tvAmount, tvDate, tvSender, tvRecipient, tvMessage, tvStatus;
    private LinearLayout llConfirmationButtons;
    private Button btnConfirm, btnDeny, btnBack;
    private FirebaseService fbDatabase;
    private AuthFirebase fAuth;
    private String transactionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        tvAmount = findViewById(R.id.tv_amount);
        tvDate = findViewById(R.id.tv_date);
        tvSender = findViewById(R.id.tv_sender);
        tvRecipient = findViewById(R.id.tv_recipient);
        tvMessage = findViewById(R.id.tv_message);
        tvStatus = findViewById(R.id.tv_status);
        llConfirmationButtons = findViewById(R.id.ll_confirmation_buttons);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnDeny = findViewById(R.id.btn_deny);
        btnBack = findViewById(R.id.btn_back);

        fbDatabase = new FirebaseService();
        fAuth = new AuthFirebase();

        transactionId = getIntent().getStringExtra("TRANSACTION_ID");

        if (transactionId != null) {
            loadTransactionDetails();
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadTransactionDetails() {
        fbDatabase.getTransactionById(transactionId, new FirebaseService.OnTransactionReceivedListener() {
            @Override
            public void onTransactionReceived(Transaction transaction) {
                if (transaction != null) {
                    tvAmount.setText(String.format(Locale.US, "$%.2f", transaction.getAmount()));
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    tvDate.setText(sdf.format(transaction.getDate()));
                    tvRecipient.setText(transaction.getRecipientEmail());
                    tvMessage.setText(transaction.getMessage());
                    tvStatus.setText(transaction.getTransactionStatus().toString());

                    fbDatabase.getUserById(transaction.getSenderId(), new FirebaseService.OnUserReceivedListener() {
                        @Override
                        public void onUserReceived(User user) {
                            if (user != null) {
                                tvSender.setText(user.getEmail());
                            } else {
                                tvSender.setText("Unknown");
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            tvSender.setText("Unknown");
                            Log.e("TransactionDetailActivity", "Error fetching sender info", e);
                        }
                    });

                    FirebaseUser currentUser = fAuth.getCurrentUser();
                    if (currentUser != null && currentUser.getEmail().equals(transaction.getRecipientEmail()) && transaction.getTransactionStatus() == TransactionStatus.Scheduled) {
                        llConfirmationButtons.setVisibility(View.VISIBLE);

                        btnConfirm.setOnClickListener(v -> {
                            transaction.setTransactionStatus(TransactionStatus.Completed);
                            transaction.setAcceptedByRecipient(true);
                            fbDatabase.updateTransaction(transaction);
                            fbDatabase.updateUserBalance(currentUser.getUid(), transaction.getAmount(), true);
                            Toast.makeText(TransactionDetailActivity.this, "Transaction confirmed!", Toast.LENGTH_SHORT).show();
                            finish();
                        });

                        btnDeny.setOnClickListener(v -> {
                            fbDatabase.updateUserBalance(transaction.getSenderId(), transaction.getAmount(), true);
                            fbDatabase.deleteTransaction(transaction);
                            Toast.makeText(TransactionDetailActivity.this, "Transaction denied!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    } else {
                        llConfirmationButtons.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(TransactionDetailActivity.this, "Transaction not found!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(TransactionDetailActivity.this, "Error loading transaction details!", Toast.LENGTH_LONG).show();
            }
        });
    }
}