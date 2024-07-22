package com.example.prm392_fe.api;

import com.example.prm392_fe.model.LoginRequest;
import com.example.prm392_fe.model.LoginResponse;
import com.example.prm392_fe.model.RegisterRequest;
import com.example.prm392_fe.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizeService {

    String AUTH = "/api";
    @POST(AUTH + "/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST(AUTH + "/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
}
