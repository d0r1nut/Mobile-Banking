package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.d0r1nut.mobilebankingproject.Services.AuthFirebase;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private TextView tvUsername;
    private Button btnLogout;
    private AuthFirebase fAuth;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUsername = view.findViewById(R.id.tv_username);
        btnLogout = view.findViewById(R.id.btn_logout);
        fAuth = new AuthFirebase();

        if (getArguments() != null) {
            userId = getArguments().getString("USER_ID");
        }

        if (userId != null) {
            FirebaseUser currentUser = fAuth.getCurrentUser();
            if (currentUser != null) {
                tvUsername.setText(currentUser.getEmail());
            }
        }

        btnLogout.setOnClickListener(v -> {
            fAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}