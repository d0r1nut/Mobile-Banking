package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.d0r1nut.mobilebankingproject.Models.ApiError;
import com.d0r1nut.mobilebankingproject.Models.CreateAccountRequest;
import com.d0r1nut.mobilebankingproject.Models.CreateAccountResponse;
import com.d0r1nut.mobilebankingproject.Services.ApiService;
import com.d0r1nut.mobilebankingproject.Services.AuthFirebase;
import com.d0r1nut.mobilebankingproject.Services.RetrofitClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etConfirmPassword, etFirstName, etLastName, etMobilePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.et_email);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etMobilePhone = findViewById(R.id.et_mobile_phone);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        Button btnSignUp = findViewById(R.id.btn_signup);
        Button btnLogin = findViewById(R.id.btn_login);

        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String mobilePhone = etMobilePhone.getText().toString();
            if (firstName.isEmpty() || lastName.isEmpty() || mobilePhone.isEmpty() || email.isEmpty() || password.isEmpty()) {
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



            CreateAccountRequest user = new CreateAccountRequest(firstName, lastName, email, mobilePhone, password);

            RetrofitClient.getInstance()
                    .create(ApiService.class)
                    .createAccount(user)
                    .enqueue(new Callback<CreateAccountResponse>() {
                        @Override
                        public void onResponse(Call<CreateAccountResponse> call, Response<CreateAccountResponse> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "User Created!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else{
                                Gson gson = new Gson();
                                ApiError apiError = gson.fromJson(response.errorBody().charStream(), ApiError.class);

                                Toast.makeText(SignUpActivity.this, apiError.getError(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CreateAccountResponse> call, Throwable t) {
                            Toast.makeText(SignUpActivity.this, "Error on creating user!", Toast.LENGTH_LONG).show();
                        }
                    });
        });

//        btnSignUp.setOnClickListener(v -> {
//            String email = etEmail.getText().toString();
//            String password = etPassword.getText().toString();
//            String confirmPassword = etConfirmPassword.getText().toString();
//
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(SignUpActivity.this, "Please fill all fields!", Toast.LENGTH_LONG).show();
//                return;
//            }
//            if (!password.equals(confirmPassword)) {
//                Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
//                return;
//            }
//            if (password.length() < 6) {
//                Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters long!", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            AuthFirebase fAuth = new AuthFirebase();
//            fAuth.signUpUser(email, password, SignUpActivity.this);
//
//            finish();
//        });

        btnLogin.setOnClickListener(v -> {
            finish();
        });
    }
}