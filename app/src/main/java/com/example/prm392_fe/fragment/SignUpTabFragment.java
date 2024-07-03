package com.example.prm392_fe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.prm392_fe.R;

public class SignUpTabFragment extends Fragment {


    EditText etEmail, etName, etPhone, etPassword, etConfirmPassword;
    Button btnSignUp;
    float v = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_register, viewGroup, false);

        etEmail = root.findViewById(R.id.etEmail);
        etPassword = root.findViewById(R.id.etPassword);
        etName = root.findViewById(R.id.etName);
        etPhone = root.findViewById(R.id.etPhone);
        etConfirmPassword = root.findViewById(R.id.etConfirmPassword);
        btnSignUp = root.findViewById(R.id.btnRegister);

        etEmail.setTranslationX(0);
        etPassword.setTranslationX(0);
        etName.setTranslationX(0);
        etPhone.setTranslationX(0);
        etConfirmPassword.setTranslationX(0);
        btnSignUp.setTranslationX(0);

        etEmail.setAlpha(v);
        etName.setAlpha(v);
        etPhone.setAlpha(v);
        etPassword.setAlpha(v);
        etConfirmPassword.setAlpha(v);
        btnSignUp.setAlpha(v);

        etEmail.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        etName.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        etPhone.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        etPassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(900).start();
        etConfirmPassword.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(900).start();
        btnSignUp.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(1100).start();

        return root;
    }
}
