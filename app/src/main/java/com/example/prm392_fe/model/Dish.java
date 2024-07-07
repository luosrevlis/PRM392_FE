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
public class Dish implements Serializable {
    private int dishID;
    private String name;
    private double price;
    private String imageUrl;
}