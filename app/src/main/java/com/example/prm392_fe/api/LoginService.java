package com.example.prm392_fe.api;

import com.example.prm392_fe.model.LoginRequest;
import com.example.prm392_fe.model.LoginResponse;
import com.example.prm392_fe.model.ProtectedResource;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {

    String AUTH = "/api/Auth";
    @POST(AUTH + "/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("protected/resource")
    Call<ProtectedResource> getProtectedResource(@Header("Authorization") String token);
}
