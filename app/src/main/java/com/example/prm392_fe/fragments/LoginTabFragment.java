package com.example.prm392_fe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.prm392_fe.R;

public class LoginTabFragment extends Fragment {

    EditText etEmail, etPassword;
    Button btnLogin;
    float v = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_login, viewGroup, false);

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

        return root;
    }
}
