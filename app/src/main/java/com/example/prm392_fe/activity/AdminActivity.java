package com.example.prm392_fe.activity;



import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_fe.R;
import com.example.prm392_fe.adapter.OrderAdapter;
import com.example.prm392_fe.api.APIClient;
import com.example.prm392_fe.api.OrderService;
import com.example.prm392_fe.model.Order;
import com.example.prm392_fe.model.OrderSearchResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "AdminActivity";
    ListView lvOrder;
    OrderAdapter orderAdapter;
    ArrayList<Order> listOrder;
    private OrderService orderService;
    private int currentPage = 1;
    private final int pageSize = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        lvOrder = findViewById(R.id.lvOrder);
        listOrder = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, listOrder);
        lvOrder.setAdapter(orderAdapter);

        orderService = APIClient.getClient(this).create(OrderService.class);

        fetchOrders(currentPage, pageSize);
    }

    private void fetchOrders(int page, int size) {
        Call<OrderSearchResponse> call = orderService.getOrderList(page, size);
        call.enqueue(new Callback<OrderSearchResponse>() {
            @Override
            public void onResponse(Call<OrderSearchResponse> call, Response<OrderSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderSearchResponse orderResponse = response.body();
                    if (orderResponse.getStatusCode() == 200) {
                        updateOrderList(orderResponse.getResult().getItems()); // Update with direct array of orders
                    } else {
                        Toast.makeText(AdminActivity.this, orderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderSearchResponse> call, Throwable throwable) {
                Toast.makeText(AdminActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error loading orders", throwable);
            }
        });
    }

    private void updateOrderList(Order[] orders) {
        listOrder.clear();
        listOrder.addAll(Arrays.asList(orders));

        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        } else {
            orderAdapter = new OrderAdapter(this, listOrder);
            lvOrder.setAdapter(orderAdapter);
        }
    }
}
