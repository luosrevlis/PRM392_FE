package com.example.prm392_fe.api;

import androidx.annotation.Nullable;

import com.example.prm392_fe.model.ListDishResponse;
import com.example.prm392_fe.model.PagedListDishResponse;
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
    @GET(DISH_ENDPOINT + "/dishes")
    Call<PagedListDishResponse> getDishesByFilter(
            @Query("Status")@Nullable Integer status,
            @Query("Name")@Nullable String name,
            @Query("MaxPrice")@Nullable Double maxPrice,
            @Query("MinPrice")@Nullable Double minPrice,
            @Query("Meal")@Nullable Integer meal,
            @Query("PageNumber")@Nullable Integer pageNumber,
            @Query("PageSize")@Nullable Integer pageSize);

    @GET(DISH_ENDPOINT + "/home")
    Call<ListDishResponse> getNewestDishes();
}
