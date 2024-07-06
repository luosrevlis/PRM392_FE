package com.example.prm392_fe.model;

public class EditAccountRequest {
    private String fullName;
    private String address;

    public EditAccountRequest(String fullName, String address) {
        this.fullName = fullName;
        this.address = address;
    }
}
