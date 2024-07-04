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
import com.example.prm392_fe.activity.AdminActivity;
import com.example.prm392_fe.activity.MainActivity;
import com.example.prm392_fe.api.AuthorizeRepository;
import com.example.prm392_fe.api.AuthorizeService;
import com.example.prm392_fe.model.LoginRequest;
import com.example.prm392_fe.model.LoginResponse;
import com.example.prm392_fe.model.LoginResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTabFragment extends Fragment {
    EditText etEmail, etPassword;
    Button btnLogin;
    float v = 0;
    private AuthorizeService AuthorizeService;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
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

        AuthorizeService = AuthorizeRepository.getAPIService(getContext());
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
        Call<LoginResponse> call = AuthorizeService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getStatusCode() == 200) {
                        LoginResult loginResult = loginResponse.getLoginResult();
                        saveToken(loginResult.getToken());
                        if (loginResult.isAdmin()){
                            navigateToAdminActivity();
                        }
                        else {
                            navigateToMainActivity();
                        }
                        Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = "Đăng nhập thất bại. Hãy thử lại sau.";
                        if (loginResponse != null) {
                            message = loginResponse.getMessage();
                        }
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Đăng nhập thất bại. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("LoginTabFragment", "Failed to login. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("LoginTabFragment", "Login failed", t);
            }
        });
    }

    private boolean validateFields() {
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Bắt buộc");
            return false;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError("Bắt buộc");
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

    private void navigateToAdminActivity(){
        Intent intent = new Intent(getActivity(), AdminActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
