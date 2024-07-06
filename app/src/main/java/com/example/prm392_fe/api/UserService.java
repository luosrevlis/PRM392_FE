package com.example.prm392_fe.api;

import com.example.prm392_fe.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    String USER_ENDPOINT = "/api/Account";
    @GET(USER_ENDPOINT+"/Current")
    Call<UserResponse> getCurrentUser();
}
