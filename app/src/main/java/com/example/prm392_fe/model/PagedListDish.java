package com.example.prm392_fe.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagedListDish {
    ArrayList<Dish> items;
    int totalPage;
    int pageSize;
    int pageNumber;
}
