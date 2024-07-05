package com.example.prm392_fe.api;

import com.example.prm392_fe.model.OrderDetailResponse;
import com.example.prm392_fe.model.OrderSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {
    String ORDER_ENDPOINT = "/orders";

    @GET(ORDER_ENDPOINT)
    Call<OrderSearchResponse> getOrderList(@Query("PageNumber") int pageNumber, @Query("PageSize") int pageSize);

    @GET(ORDER_ENDPOINT + "/{orderId}")
    Call<OrderDetailResponse> getOrderDetail(@Path("orderId") int orderId);
}
