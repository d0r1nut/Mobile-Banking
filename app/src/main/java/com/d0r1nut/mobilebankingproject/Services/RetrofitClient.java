package com.d0r1nut.mobilebankingproject.Services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://coral-app-zkmoe.ondigitalocean.app/";

    private static Retrofit instance;

    private RetrofitClient(){}

    public static synchronized Retrofit getInstance(){
        if(instance == null){
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return instance;
    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}