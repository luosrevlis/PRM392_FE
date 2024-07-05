package com.example.prm392_fe.model;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Cart implements Serializable {
    private ArrayList<CartItem> items;
    private double subtotal;
}
