package com.d0r1nut.mobilebankingproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.d0r1nut.mobilebankingproject.Models.ApiError;
import com.d0r1nut.mobilebankingproject.Models.BalanceResponse;
import com.d0r1nut.mobilebankingproject.Models.DepositRequest;
import com.d0r1nut.mobilebankingproject.Models.Transaction;
import com.d0r1nut.mobilebankingproject.Models.TransactionManager;
import com.d0r1nut.mobilebankingproject.Models.TransactionStatus;
import com.d0r1nut.mobilebankingproject.Models.TransactionType;
import com.d0r1nut.mobilebankingproject.Services.ApiService;
import com.d0r1nut.mobilebankingproject.Services.FirebaseService;
import com.d0r1nut.mobilebankingproject.Services.RetrofitClient;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTransactionActivity extends AppCompatActivity {
    private EditText etTransactionRecipient, etTransactionAmount, etTransactionMessage;
    private TextView tvSelectedDate;
    private Spinner spinnerTransactionType;
    private Button btnAddNewTransaction;
    private FirebaseService fbDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fbDatabase = new FirebaseService();

        String userId = getIntent().getStringExtra("USER_ID");
        String balanceString = getIntent().getStringExtra("BALANCE");
        String token = getIntent().getStringExtra("TOKEN");
        String email = getIntent().getStringExtra("EMAIL");

//        CalendarView calendarView = findViewById(R.id.calendar_view);
//        tvSelectedDate = findViewById(R.id.tv_selected_date);

        etTransactionRecipient = findViewById(R.id.et_add_transaction_recipient);
        etTransactionAmount = findViewById(R.id.et_add_transaction_amount);
        etTransactionMessage = findViewById(R.id.et_add_transaction_message);
        btnAddNewTransaction = findViewById(R.id.btn_add_new_transaction);

        String[] arraySpinner = new String[] {
                "Deposit", "Withdrawal", "Transfer"
        };
        Spinner spinnerTransactionType = findViewById(R.id.spinner_transaction_type);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTransactionType.setAdapter(spinnerAdapter);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                Calendar selectedDate = Calendar.getInstance();
//                selectedDate.set(year, month, dayOfMonth);
//                deadline = selectedDate.getTime();
//                tvSelectedDate.setText(dateFormat.format(deadline));
//            }
//        });

        spinnerTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                etTransactionRecipient.setVisibility(View.GONE);
                if(id == 2) { etTransactionRecipient.setVisibility(View.VISIBLE); }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                etTransactionRecipient.setVisibility(View.GONE);
            }
        });




        btnAddNewTransaction.setOnClickListener(v -> {
            String transactionRecipient = etTransactionRecipient.getText().toString();
            String transactionAmountText = etTransactionAmount.getText().toString();
            double transactionAmount;
            try {
                transactionAmount = Double.parseDouble(transactionAmountText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            String transactionMessage = etTransactionMessage.getText().toString();
            if (transactionMessage.isEmpty()) {
                transactionMessage = "No message";
            }
            String transactionType = spinnerTransactionType.getSelectedItem().toString();

            double currentBalance;
            if (balanceString == null || balanceString.isEmpty()) {
                Toast.makeText(this, "Could not retrieve balance", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                currentBalance = Double.parseDouble(balanceString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Could not parse balance", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, balanceString, Toast.LENGTH_SHORT).show();
                return;
            }

//            Transaction newTransaction;
            boolean isDeposit = false;
            if(transactionType.equals("Transfer")) { //Transfer
                if (transactionAmount > currentBalance) {
                    Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                    return;
                }
                transfer();
//                newTransaction = new Transaction(userId, transactionRecipient, new Date(), transactionAmount, transactionMessage,  TransactionType.Tranfer, TransactionStatus.Scheduled);
            }
            else if (transactionType.equals("Deposit")) { //Deposit
                if (transactionAmount > 5000) {
                    Toast.makeText(this, "Deposit amount cannot exceed $5000", Toast.LENGTH_SHORT).show();
                    return;
                }
                DepositRequest depositRequest = new DepositRequest(transactionAmount, transactionMessage, email);
                deposit(token, depositRequest);
//                newTransaction = new Transaction(userId, new Date(), transactionAmount, transactionMessage, TransactionType.Deposit, TransactionStatus.Completed);
//                isDeposit = true;
//                newTransaction.setAcceptedByRecipient(true);
            }
            else { //Withdrawal
                if (transactionAmount > currentBalance) {
                    Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                    return;
                }
                withdraw();
//                newTransaction = new Transaction(userId, new Date(), transactionAmount, transactionMessage, TransactionType.Withdrawal, TransactionStatus.Completed);
//                newTransaction.setAcceptedByRecipient(true);
            }
//            fbDatabase.addTransaction(newTransaction, isDeposit);
//            TransactionManager.getInstance().addTransaction(newTransaction);
            finish();
        });
    }

    private void deposit(String token, DepositRequest depositRequest) {
        RetrofitClient.getInstance()
                .create(ApiService.class)
                .depositFunds(token, depositRequest)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {

                            Toast.makeText(AddTransactionActivity.this, "Deposit successfully!", Toast.LENGTH_LONG).show();
                        } else {
                            Gson gson = new Gson();
                            Log.d("AddTransactionActivity", "Error: " + response.errorBody().charStream());
                            if (response.code() != 500)
                            {
                                ApiError apiError = gson.fromJson(response.errorBody().charStream(), ApiError.class);
                                Toast.makeText(AddTransactionActivity.this, apiError.getError(), Toast.LENGTH_LONG).show();
                            }
                            Log.d("AddTransactionActivity", "Error: " + response.errorBody());
                            Toast.makeText(AddTransactionActivity.this, "Error on deposit!", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(AddTransactionActivity.this, "Error on deposit!", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void withdraw() {

    }

    private void transfer() {

    }

}
