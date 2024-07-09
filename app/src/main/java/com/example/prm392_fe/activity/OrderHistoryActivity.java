package com.example.prm392_fe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_fe.R;
import com.example.prm392_fe.adapter.OrderAdapter;
import com.example.prm392_fe.api.APIClient;
import com.example.prm392_fe.api.OrderService;
import com.example.prm392_fe.model.Order;
import com.example.prm392_fe.model.OrderHistoryResponse;
import com.example.prm392_fe.model.OrderSearchResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    OrderService orderService;
    ArrayList<Order> listOrder;
    OrderAdapter orderAdapter;
    RecyclerView rvOrder;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        orderService = APIClient.getClient(this).create(OrderService.class);

        listOrder = new ArrayList<>();

        orderAdapter = new OrderAdapter(this, listOrder, false);
        orderAdapter.setOnDetailClickListener(this::navigateToOrderDetailPage);

        rvOrder = findViewById(R.id.rvOrder);
        rvOrder.setLayoutManager(new LinearLayoutManager(this));
        rvOrder.setAdapter(orderAdapter);

        fetchOrders();

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());
    }

    private void fetchOrders() {
        Call<OrderHistoryResponse> call = orderService.getOrderHistory();
        call.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderHistoryResponse orderResponse = response.body();
                    if (orderResponse.getStatusCode() == 200) {
                        listOrder.addAll(Arrays.asList(orderResponse.getResult()));
                        orderAdapter.setItems(listOrder);
                    } else {
                        Toast.makeText(OrderHistoryActivity.this, orderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("OrderHistoryActivity", orderResponse.getMessage());
                    }
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Lấy lịch sử đặt hàng thất bại. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("OrderHistoryActivity", "Server response unsuccessful.");
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable throwable) {
                Toast.makeText(OrderHistoryActivity.this, "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("OrderHistoryActivity", "Order history call failed", throwable);
            }
        });
    }

    private void navigateToOrderDetailPage(int orderId) {
        Intent intent = new Intent(OrderHistoryActivity.this, OrderDetailActivity.class);
        intent.putExtra("orderID", orderId);
        startActivity(intent);
    }
}