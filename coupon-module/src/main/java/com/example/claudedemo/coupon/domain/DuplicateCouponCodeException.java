package com.example.claudedemo.coupon.domain;

public class DuplicateCouponCodeException extends RuntimeException {
    public DuplicateCouponCodeException(String code) {
        super("Coupon code already exists: " + code);
    }
}
