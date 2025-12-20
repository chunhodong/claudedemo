package com.example.claudedemo.product.application;

import com.example.claudedemo.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String sellerId;
    private Long categoryNumber;

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getSellerId(), product.getCategoryNumber());
    }
}
