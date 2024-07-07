package com.example.prm392_fe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {
    private CreateOrderDetail[] dishes;
}
