package com.example.claudeapi.product.domain;

public class Product {

    private Long id;
    private String name;
    private String sellerId;
    private Long categoryNumber;

    public Product(Long id, String name, String sellerId, Long categoryNumber) {
        this.id = id;
        this.name = name;
        this.sellerId = sellerId;
        this.categoryNumber = categoryNumber;
    }

    public static Product create(String name, String sellerId, Long categoryNumber) {
        return new Product(null, name, sellerId, categoryNumber);
    }

    public void update(String name, String sellerId, Long categoryNumber) {
        this.name = name;
        this.sellerId = sellerId;
        this.categoryNumber = categoryNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSellerId() {
        return sellerId;
    }

    public Long getCategoryNumber() {
        return categoryNumber;
    }
}
