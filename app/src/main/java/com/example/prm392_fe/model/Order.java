package com.example.prm392_fe.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Order implements Serializable {
    private int orderID;
    private String bookingTime;
    private double bookingPrice;
    private Account account;
    private OrderDetail[] orderDetails;

    public Order(int orderID, String bookingTime, double bookingPrice, Account account, OrderDetail[] orderDetails) {
        this.orderID = orderID;
        this.bookingTime = bookingTime;
        this.bookingPrice = bookingPrice;
        this.account = account;
        this.orderDetails = orderDetails;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public double getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public OrderDetail[] getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetail[] orderDetails) {
        this.orderDetails = orderDetails;
    }
}
