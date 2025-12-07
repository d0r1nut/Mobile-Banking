package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.d0r1nut.mobilebankingproject.Models.Transaction;
import com.d0r1nut.mobilebankingproject.Models.TransactionManager;
import com.d0r1nut.mobilebankingproject.Models.TransactionStatus;
import com.d0r1nut.mobilebankingproject.Models.TransactionType;
import com.d0r1nut.mobilebankingproject.Services.FirebaseService;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Transaction newTransaction = new Transaction("7BKVmFjyjTOLW4ug19XZi22VUGb2","dima@dima.com", new Date(), 999.0, "Test transaction", TransactionType.Tranfer, TransactionStatus.Scheduled);
        TransactionManager tm = TransactionManager.getInstance();
        FirebaseService fs = new FirebaseService();
//        tm.addTransaction(newTransaction);
//        fs.addTransaction(newTransaction);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}