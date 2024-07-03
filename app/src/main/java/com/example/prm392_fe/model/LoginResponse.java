package com.example.prm392_fe.model;

public class LoginResponse {
    private int statusCode;
    private String message;
    private String result;

    public LoginResponse() {
    }

    public LoginResponse(int statusCode, String message, String result) {
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

    public String getResult() {
        return result;
    }
}
