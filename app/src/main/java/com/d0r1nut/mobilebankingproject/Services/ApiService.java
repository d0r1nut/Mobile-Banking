package com.d0r1nut.mobilebankingproject.Services;

import com.d0r1nut.mobilebankingproject.Models.ActivateAccountRequest;
import com.d0r1nut.mobilebankingproject.Models.AccountInfoResponse;
import com.d0r1nut.mobilebankingproject.Models.BalanceResponse;
import com.d0r1nut.mobilebankingproject.Models.CreateAccountRequest;
import com.d0r1nut.mobilebankingproject.Models.DepositRequest;
import com.d0r1nut.mobilebankingproject.Models.LoginRequest;
import com.d0r1nut.mobilebankingproject.Models.LoginResponse;
import com.d0r1nut.mobilebankingproject.Models.TransactionHistoryResponse;
import com.d0r1nut.mobilebankingproject.Models.TransferRequest;
import com.d0r1nut.mobilebankingproject.Models.WithdrawRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("v1/accounts/create")
    Call<Void> createAccount(@Body CreateAccountRequest createAccountRequest);

    @POST("v1/accounts/auth")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("v1/accounts/activate")
    Call<Void> activateAccount(@Body ActivateAccountRequest activateAccountRequest);

    @GET("v1/accounts/info")
    Call<List<AccountInfoResponse>> getAccountsInfo(@Header("x-access-token") String token);

    @GET("v1/accounts/info/{email}")
    Call<AccountInfoResponse> getAccountInfoByEmail(@Header("x-access-token") String token, @Path("email") String email);

    @GET("v1/accounts/balance")
    Call<BalanceResponse> getBalance(@Header("x-access-token") String token);

    @POST("v1/transactions/transfer/")
    Call<Void> transferFunds(@Header("x-access-token") String token, @Body TransferRequest transferRequest);

    @POST("v1/transactions/deposit")
    Call<Void> depositFunds(@Header("x-access-token") String token, @Body DepositRequest depositRequest);

    @POST("v1/transactions/withdraw")
    Call<Void> withdrawFunds(@Header("x-access-token") String token, @Body WithdrawRequest withdrawRequest);

    @GET("v1/transactions/{days}/statement")
    Call<TransactionHistoryResponse> getTransactionHistory(@Header("x-access-token") String token, @Path("days") int days);
}