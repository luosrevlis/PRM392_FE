package com.example.prm392_fe.model;

public class LoginResponse {
    private int statusCode;
    private String message;
    private LoginResult result;

    public LoginResponse() {
    }

    public LoginResponse(int statusCode, String message, LoginResult result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public LoginResult getLoginResult() {
        return result;
    }
}
