package com.example.prm392_fe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchResult {
    private Order[] items;
    private int totalPage;
    private int pageSize;
    private int pageNumber;
}
