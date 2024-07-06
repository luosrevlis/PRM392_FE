package com.example.prm392_fe.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.prm392_fe.R;
import com.example.prm392_fe.api.AuthorizeRepository;
import com.example.prm392_fe.model.RegisterRequest;
import com.example.prm392_fe.model.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpTabFragment extends Fragment {

    EditText etEmail, etName, etAddress, etPassword, etConfirmPassword;
    Button btnSignUp;
    ImageView ivTogglePassword, ivToogleConfirmPassowrd;
    float v = 0;
    private com.example.prm392_fe.api.AuthorizeService AuthorizeService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_register, viewGroup, false);

        etEmail = root.findViewById(R.id.etEmail);
        etPassword = root.findViewById(R.id.etPassword);
        etName = root.findViewById(R.id.etName);
        etAddress = root.findViewById(R.id.etAddress);
        etConfirmPassword = root.findViewById(R.id.etConfirmPassword);
        btnSignUp = root.findViewById(R.id.btnRegister);
        ivTogglePassword = root.findViewById(R.id.ivTogglePassword);
        ivToogleConfirmPassowrd = root.findViewById(R.id.ivToggleConfirmPassword);

        etEmail.setTranslationX(0);
        etPassword.setTranslationX(0);
        etName.setTranslationX(0);
        etAddress.setTranslationX(0);
        etConfirmPassword.setTranslationX(0);
        btnSignUp.setTranslationX(0);
        ivTogglePassword.setTranslationX(0);
        ivToogleConfirmPassowrd.setTranslationX(0);

        etEmail.setAlpha(v);
        etName.setAlpha(v);
        etAddress.setAlpha(v);
        etPassword.setAlpha(v);
        etConfirmPassword.setAlpha(v);
        btnSignUp.setAlpha(v);
        ivTogglePassword.setAlpha(v);
        ivToogleConfirmPassowrd.setAlpha(v);

        etEmail.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        etName.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        etAddress.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        etPassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(900).start();
        ivTogglePassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(900).start();
        etConfirmPassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(900).start();
        ivToogleConfirmPassowrd.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btnSignUp.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(1100).start();

        ivTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivTogglePassword.setImageResource(R.drawable.ic_eye);
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivTogglePassword.setImageResource(R.drawable.ic_eye_hidden);
                }
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        ivToogleConfirmPassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etConfirmPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivToogleConfirmPassowrd.setImageResource(R.drawable.ic_eye);
                } else {
                    etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivToogleConfirmPassowrd.setImageResource(R.drawable.ic_eye_hidden);
                }
                etConfirmPassword.setSelection(etConfirmPassword.getText().length());
            }
        });

        AuthorizeService = AuthorizeRepository.getAPIService(getContext());

        btnSignUp.setOnClickListener(v -> performRegister());

        return root;
    }

    private void performRegister(){
        String email, fullName, address, password, confirmPassword;

        email = etEmail.getText().toString().trim();
        fullName = etName.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();

        etEmail.setError(null);
        etName.setError(null);
        etAddress.setError(null);
        etPassword.setError(null);
        etConfirmPassword.setError(null);

        RegisterRequest registerRequest = new RegisterRequest(email, password, confirmPassword, fullName, address);
        List<String> errors = registerRequest.getValidationErrors();
        Call<RegisterResponse> call = AuthorizeService.register(registerRequest);

        if (!registerRequest.isValid()) {
            for (String error : errors) {
                if (error.contains("Email")) {
                    etEmail.setError(error);
                } else if (error.contains("Password") && error.contains("Confirm")) {
                    etConfirmPassword.setError(error);
                } else if (error.contains("Password")) {
                    etPassword.setError(error);
                } else if (error.contains("Full Name")) {
                    etName.setError(error);
                } else if (error.contains("Address")) {
                    etAddress.setError(error);
                }
            }
        }
        else {
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        RegisterResponse registerResponse = response.body();
                        if (registerResponse != null && registerResponse.getStatusCode() == 200) {
                            Toast.makeText(getActivity(), "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();

                            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                            viewPager.setCurrentItem(0);
                        } else {
                            String message = "Đăng ký thất bại. Hãy thử lại sau.";
                            if (registerResponse != null) {
                                message = registerResponse.getMessage();
                            }
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Đăng ký thất bại. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                        Log.e("SignUpTabFragment", "Failed to register. Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "Lỗi kết nối. Hãy thử lại sau.", Toast.LENGTH_SHORT).show();
                    Log.e("SignUpTabFragment", "Login failed", t);
                }
            });
        }
    }
}
