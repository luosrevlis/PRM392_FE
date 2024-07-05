package com.example.prm392_fe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetail {
    private int orderDetailID;
    private String dishName;
    private int quantity;
    private double price;
    private String imageUrl;
}
