package com.example.claudeapi.coupon.infrastructure;

import com.example.claudeapi.coupon.domain.Coupon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private Integer discountRate;

    private Long maxDiscountAmount;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean isActive;

    public CouponJpaEntity(Long id, String code, Integer discountRate,
                           Long maxDiscountAmount, LocalDateTime expiryDate, boolean isActive) {
        this.id = id;
        this.code = code;
        this.discountRate = discountRate;
        this.maxDiscountAmount = maxDiscountAmount;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
    }

    public static CouponJpaEntity from(Coupon coupon) {
        return new CouponJpaEntity(
            coupon.getId(),
            coupon.getCode(),
            coupon.getDiscountRate(),
            coupon.getMaxDiscountAmount(),
            coupon.getExpiryDate(),
            coupon.isActive()
        );
    }

    public Coupon toDomain() {
        return new Coupon(id, code, discountRate, maxDiscountAmount, expiryDate, isActive);
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
}
