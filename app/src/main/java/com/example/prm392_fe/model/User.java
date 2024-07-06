package com.example.prm392_fe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private String accountId;
    private String email;
    private String fullName;
    private String address;
}
