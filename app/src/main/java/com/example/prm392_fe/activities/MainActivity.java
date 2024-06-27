package com.example.prm392_fe.activities;

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
import com.example.prm392_fe.fragments.CartFragment;
import com.example.prm392_fe.fragments.HomeFragment;
import com.example.prm392_fe.fragments.RandomFragment;
import com.example.prm392_fe.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

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
        replaceFragment(new HomeFragment());
        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.mHome) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.mRandom) {
                replaceFragment(new RandomFragment());
            } else if (item.getItemId() == R.id.mCart) {
                replaceFragment(new CartFragment());
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
}