package com.example.prm392_fe.activity;

import static com.example.prm392_fe.api.APIClient.getClient;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.prm392_fe.R;
import com.example.prm392_fe.adapter.OrderDetailAdapter;
import com.example.prm392_fe.api.OrderService;
import com.example.prm392_fe.databinding.ActivityOrderDetailAdminBinding;
import com.example.prm392_fe.model.EmptyResponse;
import com.example.prm392_fe.model.Order;
import com.example.prm392_fe.model.OrderDetailResponse;
import com.example.prm392_fe.model.UpdateOrderRequest;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderDetailActivity extends AppCompatActivity {
    ActivityOrderDetailAdminBinding binding;
    OrderService orderService;
    OrderDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderDetailAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        orderService = getClient(this).create(OrderService.class);
        getOrderDetail();
    }

    private void getOrderDetail() {
        int orderID = getIntent().getIntExtra("orderID", -1);
        if (orderID < 0) {
            finish();
            return;
        }
        Call<OrderDetailResponse> call = orderService.getOrderDetail(orderID);
        call.enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(AdminOrderDetailActivity.this, "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("AdminOrderDetailActivity", "Response not successful");
                    finish();
                    return;
                }
                bindViewComponents(response.body().getResult());
            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable throwable) {
                Toast.makeText(AdminOrderDetailActivity.this, "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("AdminOrderDetailActivity", "Order detail call failed", throwable);
            }
        });
    }

    private void bindViewComponents(Order order) {
        binding.tvCustomerName.setText(order.getAccount().getFullName());
        binding.tvCustomerAddress.setText(order.getAccount().getAddress());
//        String orderDate = order.getBookingTime().substring(0, 10);
//        String orderTime = order.getBookingTime().substring(11, 19);
//        binding.tvDatetime.setText(orderDate + " " + orderTime);

        String originalFormat = "yyyy-MM-dd'T'HH:mm:ss"; // Adjust this format according to the actual format of your bookingTime
        String targetFormat = "dd/MM/yyyy HH:mm";

        SimpleDateFormat originalDateFormat = new SimpleDateFormat(originalFormat);
        SimpleDateFormat targetDateFormat = new SimpleDateFormat(targetFormat);

        try {
            Date date = originalDateFormat.parse(order.getBookingTime());
            // Add 7 hours to the date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 7);
            Date newDate = calendar.getTime();

            String formattedDate = targetDateFormat.format(newDate);
            binding.tvDatetime.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the error
        }

        DecimalFormat df = new DecimalFormat("##,###.#k");
        binding.tvSubtotal.setText("Tổng cộng: " + df.format(order.getBookingPrice() / 1000));
        adapter = new OrderDetailAdapter(this, new ArrayList<>(Arrays.asList(order.getOrderDetails())));
        binding.rvDishes.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDishes.setAdapter(adapter);
        binding.btnReturn.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        binding.btnDone.setOnClickListener(v -> {
            updateOrderStatus(order.getOrderID());
            setResult(RESULT_OK);
            finish();
        });
    }

    private void updateOrderStatus(int orderId) {
        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();
        updateOrderRequest.setOrderEvent(1);
        Call<EmptyResponse> call = orderService.updateOrderStatus(orderId, updateOrderRequest);
        call.enqueue(new Callback<EmptyResponse>() {
            @Override
            public void onResponse(Call<EmptyResponse> call, Response<EmptyResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminOrderDetailActivity.this, "Cập nhật tình trạng đơn hàng thất bại. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("AdminOrderDetailActivity", "Response unsuccessful.");
                }
                Toast.makeText(AdminOrderDetailActivity.this, "Cập nhật tình trạng đơn hàng thành công.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<EmptyResponse> call, Throwable throwable) {
                Toast.makeText(AdminOrderDetailActivity.this, "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("AdminOrderDetailActivity", "Order status update call failed", throwable);
            }
        });
    }
}