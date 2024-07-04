package com.example.prm392_fe.model;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailValid() {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPasswordValid() {
        return !TextUtils.isEmpty(password);
    }

    public boolean isValid() {
        return isEmailValid() && isPasswordValid();
    }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();

        if (!isEmailValid()) {
            errors.add(TextUtils.isEmpty(email) ? "Không được để Email trống" : "Email sai format");
        }

        if (!isPasswordValid()) {
            errors.add("Không được để Password trống");
        }

        return errors;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginRequest that = (LoginRequest) o;

        if (!email.equals(that.email)) return false;
        return password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
