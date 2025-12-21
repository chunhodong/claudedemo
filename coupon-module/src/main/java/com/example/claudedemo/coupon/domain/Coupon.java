package com.example.claudedemo.coupon.domain;

import java.time.LocalDateTime;

public class Coupon {

    private Long id;
    private String code;                // 쿠폰 코드 (예: "SUMMER2024")
    private Integer discountRate;       // 할인율 (1-100%)
    private Long maxDiscountAmount;     // 최대 할인 금액
    private LocalDateTime expiryDate;   // 만료일
    private boolean isActive;           // 활성화 상태

    public Coupon(Long id, String code, Integer discountRate,
                  Long maxDiscountAmount, LocalDateTime expiryDate, boolean isActive) {
        this.id = id;
        this.code = code;
        this.discountRate = discountRate;
        this.maxDiscountAmount = maxDiscountAmount;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
    }

    public static Coupon create(String code, Integer discountRate,
                                Long maxDiscountAmount, LocalDateTime expiryDate) {
        return new Coupon(null, code, discountRate, maxDiscountAmount, expiryDate, true);
    }

    public void update(String code, Integer discountRate,
                       Long maxDiscountAmount, LocalDateTime expiryDate, Boolean isActive) {
        this.code = code;
        this.discountRate = discountRate;
        this.maxDiscountAmount = maxDiscountAmount;
        this.expiryDate = expiryDate;
        if (isActive != null) {
            this.isActive = isActive;
        }
    }

    public void deactivate() {
        this.isActive = false;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    // Getters
    public Long getId() { return id; }
    public String getCode() { return code; }
    public Integer getDiscountRate() { return discountRate; }
    public Long getMaxDiscountAmount() { return maxDiscountAmount; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public boolean isActive() { return isActive; }
}
