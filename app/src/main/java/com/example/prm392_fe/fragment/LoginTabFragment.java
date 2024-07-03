package com.example.prm392_fe.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.prm392_fe.R;
import com.example.prm392_fe.activity.MainActivity;
import com.example.prm392_fe.api.LoginRepository;
import com.example.prm392_fe.api.LoginService;
import com.example.prm392_fe.model.LoginRequest;
import com.example.prm392_fe.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTabFragment extends Fragment {
    EditText etEmail, etPassword;
    Button btnLogin;
    float v = 0;
    private LoginService LoginService;
    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login, viewGroup, false);

        etEmail = root.findViewById(R.id.etEmail);
        etPassword = root.findViewById(R.id.etPassword);
        btnLogin = root.findViewById(R.id.btnLogin);

        etEmail.setTranslationX(0);
        etPassword.setTranslationX(0);
        btnLogin.setTranslationX(0);

        etEmail.setAlpha(v);
        etPassword.setAlpha(v);
        btnLogin.setAlpha(v);

        etEmail.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        etPassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btnLogin.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        LoginService = LoginRepository.getAPIService(getContext());
        sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

        btnLogin.setOnClickListener(v -> performLogin());

        return root;
    }

    private void performLogin() {
        if (!validateFields()) {
            return;
        }

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = LoginService.login(loginRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null && loginResponse.getStatusCode() == 200) {
                            String token = loginResponse.getResult();
                            saveToken(token);
                            navigateToMainActivity();
                            Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = "Login failed. Please try again.";
                            if (loginResponse != null) {
                                message = loginResponse.getMessage();
                            }
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                        Log.e("LoginTabFragment", "Failed to login. Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("LoginTabFragment", "Login failed", t);
                }
            });
    }

    private boolean validateFields() {
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Email is required");
            return false;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError("Password is required");
            return false;
        }
        return true;
    }

    private void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
