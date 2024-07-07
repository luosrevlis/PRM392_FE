package com.example.prm392_fe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392_fe.R;
import com.example.prm392_fe.databinding.ActivityMainBinding;
import com.example.prm392_fe.fragment.CartFragment;
import com.example.prm392_fe.fragment.HomeFragment;
import com.example.prm392_fe.fragment.RandomFragment;
import com.example.prm392_fe.fragment.SettingsFragment;
import com.example.prm392_fe.model.Cart;
import com.example.prm392_fe.model.CartItem;
import com.example.prm392_fe.model.Dish;

import java.util.ArrayList;

import vn.zalopay.sdk.ZaloPaySDK;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_RANDOM_DISH = 1;
    ActivityMainBinding binding;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.bottomNav.setOnApplyWindowInsetsListener(null);
        binding.bottomNav.setPadding(0, 0, 0, 0);

        cart = new Cart();
        cart.setItems(new ArrayList<>());

        if (getIntent().getBooleanExtra("NAVIGATE_TO_SETTINGS", false)) {
            replaceFragment(new SettingsFragment()); // Navigate to SettingsFragment
        } else {
            replaceFragment(HomeFragment.newInstance(cart));
        }
        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.mHome) {
                replaceFragment(HomeFragment.newInstance(cart));
            } else if (item.getItemId() == R.id.mRandom) {
                replaceFragment(new RandomFragment());
            } else if (item.getItemId() == R.id.mCart) {
                replaceFragment(prepareCartFragment());
            } else if (item.getItemId() == R.id.mSettings) {
                replaceFragment(new SettingsFragment());
            }
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RANDOM_DISH && resultCode == RESULT_OK && data != null) {
            CartItem newItem = (CartItem) data.getSerializableExtra("cartItem");
            if (newItem == null) {
                return;
            }
            for (CartItem currentItem : cart.getItems()) {
                if (currentItem.getDishId() == newItem.getDishId()){
                    currentItem.setQuantity(currentItem.getQuantity() + 1);
                    Toast.makeText(MainActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    replaceFragment(prepareCartFragment());
                    binding.bottomNav.setSelectedItemId(R.id.mCart);
                    return;
                }
            }
            cart.getItems().add(newItem);
            Toast.makeText(MainActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            replaceFragment(prepareCartFragment());
            binding.bottomNav.setSelectedItemId(R.id.mCart);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.clFragment, fragment);
        fragmentTransaction.commit();
    }

    private CartFragment prepareCartFragment() {
        CartFragment cartFragment = CartFragment.newInstance(cart);
        cartFragment.setOnTransactionCompletedListener(() -> {
            cart = new Cart();
            replaceFragment(HomeFragment.newInstance(cart));
        });
        return cartFragment;
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}