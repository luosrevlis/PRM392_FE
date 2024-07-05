package com.example.prm392_fe.fragment;

import static com.example.prm392_fe.api.APIClient.getClient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prm392_fe.R;
import com.example.prm392_fe.activity.MainActivity;
import com.example.prm392_fe.activity.RandomResultActivity;
import com.example.prm392_fe.api.DishService;
import com.example.prm392_fe.model.RandomDishResponse;

import java.time.LocalTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomFragment extends Fragment {
    DishService dishService;
    Spinner spinnerMeal;
    Button btnStart;

    public RandomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_random, container, false);
        dishService = getClient(getContext()).create(DishService.class);

        spinnerMeal = rootview.findViewById(R.id.spinnerMeal);
        String[] meals = new String[] {"Bữa sáng", "Bữa trưa", "Bữa tối"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, meals);
        spinnerMeal.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int hour = LocalTime.now().getHour();
            if (hour < 9) {
                spinnerMeal.setSelection(0);
            } else if (hour < 15) {
                spinnerMeal.setSelection(1);
            } else {
                spinnerMeal.setSelection(2);
            }
        }

        btnStart = rootview.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> getRandomDish());

        // Inflate the layout for this fragment
        return rootview;
    }

    private void getRandomDish() {
        int meal = spinnerMeal.getSelectedItemPosition();
        Call<RandomDishResponse> call = dishService.getRandomDish(meal);
        call.enqueue(new Callback<RandomDishResponse>() {
            @Override
            public void onResponse(Call<RandomDishResponse> call, Response<RandomDishResponse> response) {
                RandomDishResponse body = response.body();
                if (body == null) {
                    Toast.makeText(getActivity(), "Lỗi server. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("RandomFragment", "Response body is null");
                    return;
                }
                Intent intent = new Intent(getActivity(), RandomResultActivity.class);
                intent.putExtra("randomDish", body.getResult());
                intent.putExtra("meal", meal);
                getActivity().startActivityForResult(intent, MainActivity.REQUEST_CODE_RANDOM_DISH);
            }

            @Override
            public void onFailure(Call<RandomDishResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("RandomFragment", "Random call failed", throwable);
            }
        });
    }
}