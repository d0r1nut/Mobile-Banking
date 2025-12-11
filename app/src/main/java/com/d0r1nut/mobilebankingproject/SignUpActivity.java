package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.d0r1nut.mobilebankingproject.Services.AuthFirebase;

public class SignUpActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        Button btnSignUp = findViewById(R.id.btn_signup);
        Button btnLogin = findViewById(R.id.btn_login);

        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill all fields!", Toast.LENGTH_LONG).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters long!", Toast.LENGTH_LONG).show();
                return;
            }

            AuthFirebase fAuth = new AuthFirebase();
            fAuth.signUpUser(email, password, SignUpActivity.this);

            finish();
        });

        btnLogin.setOnClickListener(v -> {
            finish();
        });
    }
}