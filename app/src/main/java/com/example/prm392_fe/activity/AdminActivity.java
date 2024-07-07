package com.example.prm392_fe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_fe.R;
import com.example.prm392_fe.adapter.OrderAdapter;
import com.example.prm392_fe.api.APIClient;
import com.example.prm392_fe.api.OrderService;
import com.example.prm392_fe.fragment.CartFragment;
import com.example.prm392_fe.model.CartItem;
import com.example.prm392_fe.model.EmptyResponse;
import com.example.prm392_fe.model.Order;
import com.example.prm392_fe.model.OrderSearchResponse;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_ORDER_DETAIL = 1;
    private static final String TAG = "AdminActivity";
    ListView lvOrder;
    OrderAdapter orderAdapter;
    Button btnLogout;
    ArrayList<Order> listOrder;
    private OrderService orderService;
    private int currentPage = 1;
    private final int pageSize = 2;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        lvOrder = findViewById(R.id.lvOrder);
        btnLogout = findViewById(R.id.btnLogout);

        orderService = APIClient.getClient(this).create(OrderService.class);

        listOrder = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, listOrder);
        orderAdapter.setOnDoneClickListener(this::updateOrderStatus);
        orderAdapter.setOnDetailClickListener(this::navigateToOrderDetailPage);
        lvOrder.setAdapter(orderAdapter);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ORDER_DETAIL && resultCode == RESULT_OK) {
            fetchOrders(currentPage, pageSize);
        }
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
//                        listOrder.clear(); // clear old data
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

    private void navigateToOrderDetailPage(int orderId) {
        Intent intent = new Intent(AdminActivity.this, OrderDetailActivity.class);
        intent.putExtra("orderID", orderId);
        startActivityForResult(intent, REQUEST_CODE_ORDER_DETAIL);
    }

    private void updateOrderStatus(int orderId) {
        Call<EmptyResponse> call = orderService.updateOrderStatus(orderId);
        call.enqueue(new Callback<EmptyResponse>() {
            @Override
            public void onResponse(Call<EmptyResponse> call, Response<EmptyResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminActivity.this, "Cập nhật tình trạng đơn hàng thất bại. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("AdminActivity", "Response unsuccessful.");
                }
                Toast.makeText(AdminActivity.this, "Cập nhật tình trạng đơn hàng thành công.", Toast.LENGTH_SHORT).show();
                fetchOrders(currentPage, pageSize);
            }

            @Override
            public void onFailure(Call<EmptyResponse> call, Throwable throwable) {
                Toast.makeText(AdminActivity.this, "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("AdminActivity", "Order status update call failed", throwable);
            }
        });
    }

    private void logOut(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
