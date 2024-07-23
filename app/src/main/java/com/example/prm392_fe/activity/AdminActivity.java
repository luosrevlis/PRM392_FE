package com.example.prm392_fe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.example.prm392_fe.model.EmptyResponse;
import com.example.prm392_fe.model.Order;
import com.example.prm392_fe.model.OrderSearchResponse;
import com.example.prm392_fe.model.UpdateOrderRequest;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_ORDER_DETAIL = 1;
    private static final String TAG = "AdminActivity";
    RecyclerView rvOrder;
    LinearLayoutManager linearLayoutManager;
    OrderAdapter orderAdapter;
    Button btnLogout;
    ArrayList<Order> listOrder;
    private OrderService orderService;
    private int currentPage = 1;
    private final int pageSize = 4;
    private boolean isLoading = false;
    private boolean isEndOfList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvOrder = findViewById(R.id.rvOrder);
        btnLogout = findViewById(R.id.btnLogout);

        orderService = APIClient.getClient(this).create(OrderService.class);

        listOrder = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, listOrder, true);
        orderAdapter.setOnDoneClickListener(this::updateOrderStatus);
        orderAdapter.setOnDetailClickListener(this::navigateToOrderDetailPage);
        linearLayoutManager = new LinearLayoutManager(this);
        rvOrder.setLayoutManager(linearLayoutManager);
        rvOrder.setAdapter(orderAdapter);

        fetchOrders(currentPage, pageSize);

        btnLogout.setOnClickListener(v -> logOut());

        rvOrder.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy == 0 || isEndOfList || isLoading) {
                    return;
                }

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (visibleItemCount + pastVisibleItems >= totalItemCount) {
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
            listOrder.clear();
            currentPage = 1;
            isEndOfList = false;
            fetchOrders(currentPage, pageSize);
            orderAdapter.notifyDataSetChanged();
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
                    if (orderResponse.getResult().getItems().length == 0) {
                        isEndOfList = true;
                        Toast.makeText(AdminActivity.this, "Đã xem hết các đơn hàng!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (orderResponse.getStatusCode() == 200) {
                        addOrdersToList(orderResponse.getResult().getItems()); // Append new data
                    } else {
                        Toast.makeText(AdminActivity.this, orderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, orderResponse.getMessage());
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "Lấy danh sách đơn hàng thất bại. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Server response unsuccessful.");
                }
            }

            @Override
            public void onFailure(Call<OrderSearchResponse> call, Throwable throwable) {
                isLoading = false;
                Toast.makeText(AdminActivity.this, "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Order history call failed", throwable);
            }
        });
    }

    private void addOrdersToList(Order[] orders) {
        listOrder.addAll(Arrays.asList(orders));
        orderAdapter.setItems(listOrder);
    }

    private void navigateToOrderDetailPage(int orderId) {
        Intent intent = new Intent(AdminActivity.this, AdminOrderDetailActivity.class);
        intent.putExtra("orderID", orderId);
        startActivityForResult(intent, REQUEST_CODE_ORDER_DETAIL);
    }

    private void updateOrderStatus(int orderId) {
        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();
        updateOrderRequest.setOrderEvent(1);
        Call<EmptyResponse> call = orderService.updateOrderStatus(orderId, updateOrderRequest);
        call.enqueue(new Callback<EmptyResponse>() {
            @Override
            public void onResponse(Call<EmptyResponse> call, Response<EmptyResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminActivity.this, "Cập nhật tình trạng đơn hàng thất bại. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("AdminActivity", "Response unsuccessful.");
                }
                Toast.makeText(AdminActivity.this, "Cập nhật tình trạng đơn hàng thành công.", Toast.LENGTH_SHORT).show();
                listOrder.clear();
                currentPage = 1;
                isEndOfList = false;
                fetchOrders(currentPage, pageSize);
                orderAdapter.notifyDataSetChanged();
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
