package com.example.prm392_fe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response<T> {
    private int statusCode;
    private String message;
    private T result;
}
