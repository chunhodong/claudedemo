package com.example.claudedemo.coupon.domain;

public class ExpiredCouponException extends RuntimeException {
    public ExpiredCouponException(String code) {
        super("Coupon expired: " + code);
    }
}
