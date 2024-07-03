package com.example.prm392_fe.api;

import com.example.prm392_fe.model.RandomDishResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface DishService {
    String DISH_ENDPOINT = "/api/Dish";

    @GET(DISH_ENDPOINT + "/random")
    Call<RandomDishResponse> getRandomDish(@Query("meal") int meal);
}
