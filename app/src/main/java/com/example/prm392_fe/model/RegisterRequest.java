package com.example.prm392_fe.model;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;
    private String address;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    public RegisterRequest() {}

    public RegisterRequest(String email, String password, String confirmPassword, String fullName, String address) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.fullName = fullName;
        this.address = address;
    }

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean passwordsMatch() {
        return this.password.equals(this.confirmPassword);
    }

    public boolean isEmailValid() {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPasswordValid() {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    public boolean isConfirmPasswordValid() {
        return !TextUtils.isEmpty(confirmPassword);
    }

    public boolean isFullNameValid() {
        return !TextUtils.isEmpty(fullName);
    }

    public boolean isAddressValid() {
        return !TextUtils.isEmpty(address);
    }

    public boolean isValid() {
        return isEmailValid() && isPasswordValid() && isConfirmPasswordValid() && passwordsMatch()
                && isFullNameValid() && isAddressValid();
    }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();

        if (TextUtils.isEmpty(email)) {
            errors.add("Không được để Email trống");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.add("Email sai format");
        }

        if (TextUtils.isEmpty(password)) {
            errors.add("Không được để Password trống");
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.add("Password phải từ 8 ký tự trở lên, 1 chữ in hoa, 1 chữ thường, 1 số, và 1 ký tự đặc biệt");
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            errors.add("Không được để Confirm Password trống");
        }

        if (!passwordsMatch()) {
            errors.add("Confirm Passwords không trùng với Password");
        }

        if (TextUtils.isEmpty(fullName)) {
            errors.add("Không được để Tên trống");
        }

        if (TextUtils.isEmpty(address)) {
            errors.add("Không được để Địa chỉ trống");
        }

        return errors;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterRequest that = (RegisterRequest) o;

        if (!email.equals(that.email)) return false;
        if (!password.equals(that.password)) return false;
        if (!confirmPassword.equals(that.confirmPassword)) return false;
        if (!fullName.equals(that.fullName)) return false;
        return address.equals(that.address);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + confirmPassword.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }
}
