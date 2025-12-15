package com.testing.inventorytracking;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    public Retrofit connect(String Base_Url , String username , String password) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .connectTimeout(30, TimeUnit.SECONDS)  // Reduced from 3 minutes to 30 seconds
                .readTimeout(30, TimeUnit.SECONDS)     // Reduced from 3 minutes to 30 seconds
                .writeTimeout(30, TimeUnit.SECONDS)    // Added write timeout
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build() ;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }


}
