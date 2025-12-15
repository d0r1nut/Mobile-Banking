package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.d0r1nut.mobilebankingproject.Models.AccountWrapper;
import com.d0r1nut.mobilebankingproject.Models.ApiError;
import com.d0r1nut.mobilebankingproject.Services.ApiService;
import com.d0r1nut.mobilebankingproject.Services.AuthFirebase;
import com.d0r1nut.mobilebankingproject.Services.RetrofitClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView tvFirstName, tvLastName, tvMobilePhone, tvEmail, tvAccountId;
    private Button btnLogout;
    private AuthFirebase fAuth;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvFirstName = view.findViewById(R.id.tv_first_name);
        tvLastName = view.findViewById(R.id.tv_last_name);
        tvMobilePhone = view.findViewById(R.id.tv_mobile_phone);
        tvEmail = view.findViewById(R.id.tv_email);

        btnLogout = view.findViewById(R.id.btn_logout);


        String token = getArguments().getString("TOKEN");
        String email = getArguments().getString("EMAIL");

        Gson gson = new Gson();

        RetrofitClient.getInstance()
                .create(ApiService.class)
                .getAccountInfoByEmail(token, email)
                .enqueue(new Callback<AccountWrapper>() {
                    @Override
                    public void onResponse(Call<AccountWrapper> call, Response<AccountWrapper> response) {
                        if (response.isSuccessful()) {
                            AccountWrapper accountInfoResponse = response.body();

                            tvFirstName.setText(accountInfoResponse.getAccount().getFirstName());
                            tvLastName.setText(accountInfoResponse.getAccount().getLastName());
                            tvMobilePhone.setText(accountInfoResponse.getAccount().getMobilePhone());
                            tvEmail.setText(accountInfoResponse.getAccount().getEmail());

                            Toast.makeText(getActivity(), "User got successfully!", Toast.LENGTH_LONG).show();
                        } else {
                            ApiError apiError = gson.fromJson(response.errorBody().charStream(), ApiError.class);
                            Toast.makeText(getActivity(), apiError.getError(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<AccountWrapper> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error on Login user!", Toast.LENGTH_LONG).show();
                    }
                });

//        fAuth = new AuthFirebase();
//        if (getArguments() != null) {
//            userId = getArguments().getString("USER_ID");
//        }
//
//        if (userId != null) {
//            FirebaseUser currentUser = fAuth.getCurrentUser();
//            if (currentUser != null) {
//                tvUsername.setText(currentUser.getEmail());
//            }
//        }

        btnLogout.setOnClickListener(v -> {
//            fAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}