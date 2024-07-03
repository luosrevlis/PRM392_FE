package com.example.prm392_fe.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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

public class MainActivity extends AppCompatActivity {
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

        replaceFragment(new HomeFragment());
        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.mHome) {
                replaceFragment(new HomeFragment());
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

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.clFragment, fragment);
        fragmentTransaction.commit();
    }

    private CartFragment prepareCartFragment() {
        ArrayList<CartItem> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            items.add(new CartItem(1, 1, new Dish(1, "Dish name", 100000, "https://picsum.photos/200/200")));
        }
        cart.setItems(items);
        return CartFragment.newInstance(cart);
    }
}