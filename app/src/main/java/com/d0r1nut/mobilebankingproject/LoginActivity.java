package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.d0r1nut.mobilebankingproject.Models.User;
import com.d0r1nut.mobilebankingproject.Services.AuthFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnSignUp = findViewById(R.id.btn_signup);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Please fill all fields!", Toast.LENGTH_LONG).show();
                return;
            }

            AuthFirebase fAuth = new AuthFirebase();
            fAuth.signInUser(email, password, LoginActivity.this, result -> {
                if(result){
                    String userId = fAuth.getCurrentUser().getUid();

                    Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    loginIntent.putExtra("USER_ID", userId);
                    startActivity(loginIntent);

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                    userRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            if (!snapshot.exists()) {
                                User user = new User(userId, email);
                                userRef.setValue(user);
                            }
                        } else {
                            Log.e("LoginActivity", "Error checking if user exists", task.getException());
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
                }
            });


        });

        btnSignUp.setOnClickListener(v -> {
            Intent signupIntent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(signupIntent);
        });
    }
}