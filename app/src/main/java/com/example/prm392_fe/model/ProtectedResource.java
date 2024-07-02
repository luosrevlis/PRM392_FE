package com.example.prm392_fe.model;

import com.google.gson.annotations.SerializedName;

public class ProtectedResource {
    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
