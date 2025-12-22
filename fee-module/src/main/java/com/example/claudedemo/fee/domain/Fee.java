package com.example.claudedemo.fee.domain;

public class Fee {

    private Long id;
    private Long productNumber;
    private Long feeValue;
    private FeeUnit feeUnit;

    public Fee(Long id, Long productNumber, Long feeValue, FeeUnit feeUnit) {
        this.id = id;
        this.productNumber = productNumber;
        this.feeValue = feeValue;
        this.feeUnit = feeUnit;
    }

    public static Fee create(Long productNumber, Long feeValue, FeeUnit feeUnit) {
        return new Fee(null, productNumber, feeValue, feeUnit);
    }

    public void update(Long productNumber, Long feeValue, FeeUnit feeUnit) {
        this.productNumber = productNumber;
        this.feeValue = feeValue;
        this.feeUnit = feeUnit;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getProductNumber() {
        return productNumber;
    }

    public Long getFeeValue() {
        return feeValue;
    }

    public FeeUnit getFeeUnit() {
        return feeUnit;
    }
}
