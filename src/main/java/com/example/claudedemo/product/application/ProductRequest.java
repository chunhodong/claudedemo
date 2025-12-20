package com.example.claudedemo.product.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private Long id;
    private String name;
    private String sellerId;
    private Long categoryNumber;
}
