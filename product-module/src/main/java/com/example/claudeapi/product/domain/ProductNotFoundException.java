package com.example.claudeapi.product.domain;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Product not found with id: " + id);
    }
}
