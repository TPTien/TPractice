package com.tptien.tpractice.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRetrofitClient {
     private static Retrofit retrofit =null;
     public static Retrofit getAPIClient(String baseUrl){
         OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(10000, TimeUnit.MILLISECONDS)
                                                                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                                                                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                                                                .retryOnConnectionFailure(true)
                                                                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                                                                .build();

         Gson gson =new GsonBuilder().setLenient().create();
         retrofit = new Retrofit.Builder()
                                .baseUrl(baseUrl)
                                .client(okHttpClient)
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();
         return retrofit;
     }
}

