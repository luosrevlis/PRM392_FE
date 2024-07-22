package com.example.prm392_fe.api;

import com.example.prm392_fe.model.CreateOrderRequest;
import com.example.prm392_fe.model.CreateOrderResponse;
import com.example.prm392_fe.model.EmptyResponse;
import com.example.prm392_fe.model.OrderDetailResponse;
import com.example.prm392_fe.model.OrderHistoryResponse;
import com.example.prm392_fe.model.OrderSearchResponse;
import com.example.prm392_fe.model.UpdateOrderRequest;
import com.example.prm392_fe.model.UpdateTransactionRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {
    String ORDER_ENDPOINT = "/api/orders";

    @GET(ORDER_ENDPOINT)
    Call<OrderSearchResponse> getOrderList(@Query("PageNumber") int pageNumber, @Query("PageSize") int pageSize);

    @GET(ORDER_ENDPOINT + "/{orderId}")
    Call<OrderDetailResponse> getOrderDetail(@Path("orderId") int orderId);

    @POST(ORDER_ENDPOINT)
    Call<CreateOrderResponse> createOrder(@Body CreateOrderRequest request);

    @PUT(ORDER_ENDPOINT + "/{orderId}/transaction")
    Call<EmptyResponse> updateTransaction(@Path("orderId") int orderId, @Body UpdateTransactionRequest request);

    @PUT(ORDER_ENDPOINT + "/{orderId}")
    Call<EmptyResponse> updateOrderStatus(@Path("orderId") int orderId, @Body UpdateOrderRequest request);

    @GET(ORDER_ENDPOINT + "/current")
    Call<OrderHistoryResponse> getOrderHistory();
}
