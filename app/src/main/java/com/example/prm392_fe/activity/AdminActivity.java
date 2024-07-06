package com.example.prm392_fe.activity;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Button;
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
    Button btnLogout;
    ArrayList<Order> listOrder;
    private OrderService orderService;
    private int currentPage = 1;
    private final int pageSize = 10;
    private boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        lvOrder = findViewById(R.id.lvOrder);
        btnLogout = findViewById(R.id.btnLogout);

        listOrder = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, listOrder);
        lvOrder.setAdapter(orderAdapter);

        orderService = APIClient.getClient(this).create(OrderService.class);

        fetchOrders(currentPage, pageSize);

        btnLogout.setOnClickListener(v -> logOut());

        lvOrder.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && !isLoading) {
                    currentPage++;
                    fetchOrders(currentPage, pageSize);
                }
            }
        });
    }

    private void fetchOrders(int page, int size) {
        isLoading = true;
        Call<OrderSearchResponse> call = orderService.getOrderList(page, size);
        call.enqueue(new Callback<OrderSearchResponse>() {
            @Override
            public void onResponse(Call<OrderSearchResponse> call, Response<OrderSearchResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    OrderSearchResponse orderResponse = response.body();
                    if (orderResponse.getStatusCode() == 200) {
                        addOrdersToList(orderResponse.getResult().getItems()); // Append new data
                    } else {
                        Toast.makeText(AdminActivity.this, orderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderSearchResponse> call, Throwable throwable) {
                isLoading = false;
                Toast.makeText(AdminActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error loading orders", throwable);
            }
        });
    }

    private void addOrdersToList(Order[] orders) {
        listOrder.addAll(Arrays.asList(orders));
        orderAdapter.notifyDataSetChanged();
    }

    private void logOut(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
