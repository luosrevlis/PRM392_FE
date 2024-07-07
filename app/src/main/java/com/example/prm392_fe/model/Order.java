package com.example.prm392_fe.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private int orderID;
    private String bookingTime;
    private double bookingPrice;
    private Account account;
    private OrderDetail[] orderDetails;
}
