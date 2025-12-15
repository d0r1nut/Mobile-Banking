package com.d0r1nut.mobilebankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.d0r1nut.mobilebankingproject.Adapters.TransactionAdapter;
import com.d0r1nut.mobilebankingproject.Models.ApiError;
import com.d0r1nut.mobilebankingproject.Models.BalanceResponse;
import com.d0r1nut.mobilebankingproject.Models.StatementWrapper;
import com.d0r1nut.mobilebankingproject.Models.Transaction;
import com.d0r1nut.mobilebankingproject.Models.TransactionHistoryResponse;
import com.d0r1nut.mobilebankingproject.Services.ApiService;
import com.d0r1nut.mobilebankingproject.Services.FirebaseService;
import com.d0r1nut.mobilebankingproject.Services.RetrofitClient;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsFragment extends Fragment {

    private RecyclerView rvTransactions;
    private TransactionAdapter transactionAdapter;
    private FirebaseService fbDatabase;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        rvTransactions = view.findViewById(R.id.rv_transactions);

        String token = getArguments().getString("TOKEN");
//        Log.d("TransactionsFragment", "Token: " + token);

        RetrofitClient.getInstance()
                .create(ApiService.class)
                .getTransactionHistory(token, 30)
                .enqueue(new Callback<StatementWrapper>() {
                    @Override
                    public void onResponse(Call<StatementWrapper> call, Response<StatementWrapper> response) {
                        if (response.isSuccessful()) {
                            StatementWrapper transactions = response.body();

//                            Log.d("TransactionsFragment", "Transactions: " + transactions.getTransactions());

//                            if (transactionAdapter == null) {
//                                transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
//                                transactionAdapter = new TransactionAdapter(transactions, transaction -> {
//                                    Intent intent = new Intent(getActivity(), TransactionDetailActivity.class);
//                                    intent.putExtra("TRANSACTION_ID", transaction.getTransactionId());
//                                    startActivity(intent);
//                                });
//                                rvTransactions.setAdapter(transactionAdapter);
//                                rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
//                            } else {
//                                transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
//                                transactionAdapter.updateTransaction(transactions);
//                            }

                            Toast.makeText(getActivity(), "Transactions loaded successfully!", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("TransactionsFragment", response.errorBody().toString());
                            Gson gson = new Gson();
                            ApiError apiError = gson.fromJson(response.errorBody().charStream(), ApiError.class);
                            Toast.makeText(getActivity(), apiError.getError(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<StatementWrapper> call, Throwable t) {
                        Log.d("TransactionsFragment", call.request().toString());
                        Log.e("TransactionsFragment", "Error on loading transactions!", t);
                        Toast.makeText(getActivity(), "Error on loading transactions!", Toast.LENGTH_LONG).show();
                    }
                });



//        fbDatabase = new FirebaseService();
//
//        if (getArguments() != null) {
//            userId = getArguments().getString("USER_ID");
//        }
//
//        if (userId != null) {
//            fbDatabase.getTransactionForUser(userId, new FirebaseService.OnTransactionsReceivedListener() {
//                @Override
//                public void onTransactionsReceived(List<Transaction> transactions) {
//                    if (transactionAdapter == null) {
//                        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
//                        transactionAdapter = new TransactionAdapter(transactions, transaction -> {
//                            Intent intent = new Intent(getActivity(), TransactionDetailActivity.class);
//                            intent.putExtra("TRANSACTION_ID", transaction.getTransactionId());
//                            startActivity(intent);
//                        });
//                        rvTransactions.setAdapter(transactionAdapter);
//                        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
//                    } else {
//                        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
//                        transactionAdapter.updateTransaction(transactions);
//                    }
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    Toast.makeText(getContext(), "Error loading transactions!", Toast.LENGTH_LONG).show();
//                }
//            });
//        }

        return view;
    }
}