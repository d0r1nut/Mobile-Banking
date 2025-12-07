package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.d0r1nut.mobilebankingproject.Models.User;
import com.d0r1nut.mobilebankingproject.Services.AuthFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText etUserId, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private FirebaseDatabase fbDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        etUserId = findViewById(R.id.et_user_id);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        btnSignUp = findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();


            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(SignUpActivity.this, "Please fill all fields!", Toast.LENGTH_LONG).show();
                return;
            }
            if(!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.length() < 6) {
                Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters long!", Toast.LENGTH_LONG).show();
                return;
            }

            AuthFirebase fAuth = new AuthFirebase();
            fAuth.signUpUser(email, password, SignUpActivity.this);

            finish();
        });
    }
}