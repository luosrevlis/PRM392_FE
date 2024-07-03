package com.example.prm392_fe.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {
    private int dishId;
    private int quantity;
    private Dish dish;
}
