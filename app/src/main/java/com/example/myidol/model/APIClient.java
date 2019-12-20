package com.example.myidol.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    //private static final String BASE_URL = "http://sonhaui.000webhostapp.com/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {

        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }
}
