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

import com.d0r1nut.mobilebankingproject.Models.Transaction;
import com.d0r1nut.mobilebankingproject.Models.TransactionManager;
import com.d0r1nut.mobilebankingproject.Models.TransactionStatus;
import com.d0r1nut.mobilebankingproject.Models.TransactionType;
import com.d0r1nut.mobilebankingproject.Services.FirebaseService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
                    if(id == 2) {
                        etTransactionRecipient.setVisibility(View.VISIBLE);
                    }
                    else {
                        etTransactionRecipient.setVisibility(View.GONE);
                    }
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
                return;
            }

            Transaction newTransaction;
            if(transactionType.equals("Transfer")) {
                if (transactionAmount > currentBalance) {
                    Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                    return;
                }
                newTransaction = new Transaction(userId, transactionRecipient, new Date(), transactionAmount, transactionMessage,  TransactionType.Tranfer, TransactionStatus.Scheduled);
            }
            else if (transactionType.equals("Deposit")) {
                if (transactionAmount > 5000) {
                    Toast.makeText(this, "Deposit amount cannot exceed $5000", Toast.LENGTH_SHORT).show();
                    return;
                }
                newTransaction = new Transaction(userId, new Date(), transactionAmount, transactionMessage, TransactionType.Deposit, TransactionStatus.Completed);
                newTransaction.setAcceptedByRecipient(true);
            }
            else { //Withdrawal
                if (transactionAmount > currentBalance) {
                    Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                    return;
                }
                newTransaction = new Transaction(userId, new Date(), transactionAmount, transactionMessage, TransactionType.Withdrawal, TransactionStatus.Completed);
                newTransaction.setAcceptedByRecipient(true);
            }
            fbDatabase.addTransaction(newTransaction);
            TransactionManager.getInstance().addTransaction(newTransaction);
            finish();
        });
    }
}
