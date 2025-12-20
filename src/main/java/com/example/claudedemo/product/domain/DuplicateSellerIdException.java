package com.example.claudedemo.product.domain;

public class DuplicateSellerIdException extends RuntimeException {

    public DuplicateSellerIdException(String sellerId) {
        super("Seller ID already exists: " + sellerId);
    }
}
