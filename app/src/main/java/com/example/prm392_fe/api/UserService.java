package com.example.prm392_fe.api;

import com.example.prm392_fe.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    String USER_ENDPOINT = "/api/accounts";
    @GET(USER_ENDPOINT+"/current")
    Call<UserResponse> getCurrentUser();
}
