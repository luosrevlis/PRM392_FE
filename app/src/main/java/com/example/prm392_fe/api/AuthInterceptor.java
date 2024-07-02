package com.example.prm392_fe.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();

        if (token != null) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
