package com.example.prm392_fe.activity;

import static com.example.prm392_fe.api.APIClient.getClient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_fe.R;
import com.example.prm392_fe.adapter.CartItemAdapter;
import com.example.prm392_fe.api.DishService;
import com.example.prm392_fe.model.CartItem;
import com.example.prm392_fe.model.Dish;
import com.example.prm392_fe.model.RandomDishResponse;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomResultActivity extends AppCompatActivity {
    ArrayList<CartItem> items;
    double subtotal;
    CartItemAdapter cartItemAdapter;
    RecyclerView rvDishes;
    TextView tvSubtotalValue;
    FrameLayout btnAdd;
    FrameLayout btnRedo;
    DishService dishService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_random_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dishService = getClient(this).create(DishService.class);

        Dish dish = (Dish) getIntent().getSerializableExtra("randomDish");
        if (dish == null) {
            finish();
            return;
        }

        items = new ArrayList<>();
        items.add(new CartItem(dish.getDishId(), 1, dish));

        cartItemAdapter = new CartItemAdapter(this, items);
        cartItemAdapter.setIncListener(position -> updateSubtotal());
        cartItemAdapter.setDecListener(position -> updateSubtotal());
        cartItemAdapter.setQuantityListener(position -> updateSubtotal());
        cartItemAdapter.setCloseListener(position -> updateSubtotal());

        rvDishes = findViewById(R.id.rvDishes);
        rvDishes.setLayoutManager(new LinearLayoutManager(this));
        rvDishes.setAdapter(cartItemAdapter);

        tvSubtotalValue = findViewById(R.id.tvSubtotalValue);
        updateSubtotal();

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> addToCart());

        btnRedo = findViewById(R.id.btnRedo);
        btnRedo.setOnClickListener(v-> getRandomDish());
    }

    private void updateSubtotal() {
        subtotal = 0;
        for (CartItem item: items) {
            subtotal += item.getDish().getPrice() * item.getQuantity();
        }
        if (subtotal == 0) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        tvSubtotalValue.setText(String.format(Locale.ENGLISH, "%.1fk", subtotal / 1000));
    }

    private void addToCart() {
        Intent intent = new Intent();

        intent.putExtra("cartItem", items.get(0));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getRandomDish() {
        int meal = getIntent().getIntExtra("meal", 0);
        Call<RandomDishResponse> call = dishService.getRandomDish(meal);
        call.enqueue(new Callback<RandomDishResponse>() {
            @Override
            public void onResponse(Call<RandomDishResponse> call, Response<RandomDishResponse> response) {
                RandomDishResponse body = response.body();
                if (body == null) {
                    Toast.makeText(RandomResultActivity.this, "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("RandomFragment", "Response body is null");
                    return;
                }
                Dish dish = body.getResult();

                items = new ArrayList<>();
                items.add(new CartItem(dish.getDishId(), 1, dish));

                cartItemAdapter = new CartItemAdapter(RandomResultActivity.this, items);
                cartItemAdapter.setIncListener(position -> updateSubtotal());
                cartItemAdapter.setDecListener(position -> updateSubtotal());
                cartItemAdapter.setQuantityListener(position -> updateSubtotal());

                rvDishes.setLayoutManager(new LinearLayoutManager(RandomResultActivity.this));
                rvDishes.setAdapter(cartItemAdapter);

                updateSubtotal();
            }

            @Override
            public void onFailure(Call<RandomDishResponse> call, Throwable throwable) {
                Toast.makeText(RandomResultActivity.this, "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("RandomFragment", "Random call failed", throwable);
            }
        });
    }
}