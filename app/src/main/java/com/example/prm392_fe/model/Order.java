package com.example.prm392_fe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private int orderID;
    private String bookingTime;
    private double bookingPrice;
    private Account account;
    private OrderDetail[] orderDetails;
}
