package com.example.storegame.Api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static String BASE_URL = "https://api.cirno.click/";



    public static Retrofit getRetrofit(String token) {
        Retrofit retrofit;
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        if ( token.equals("")) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public class TokenInterceptor implements Interceptor {
        String token;

        public TokenInterceptor(String token) {
            this.token = token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {

            //rewrite the request to add bearer token
            Request newRequest = chain.request().newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();

            return chain.proceed(newRequest);
        }
    }

}
