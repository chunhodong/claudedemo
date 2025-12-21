package com.example.claudedemo.coupon.application;

import com.example.claudedemo.coupon.domain.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "쿠폰 응답 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {

    @Schema(description = "쿠폰 ID", example = "1")
    private Long id;

    @Schema(description = "쿠폰 코드", example = "SUMMER2024")
    private String code;

    @Schema(description = "할인율", example = "20")
    private Integer discountRate;

    @Schema(description = "최대 할인 금액", example = "10000")
    private Long maxDiscountAmount;

    @Schema(description = "만료일", example = "2024-12-31T23:59:59")
    private LocalDateTime expiryDate;

    @Schema(description = "활성화 상태", example = "true")
    private Boolean isActive;

    @Schema(description = "만료 여부", example = "false")
    private Boolean isExpired;

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
            coupon.getId(),
            coupon.getCode(),
            coupon.getDiscountRate(),
            coupon.getMaxDiscountAmount(),
            coupon.getExpiryDate(),
            coupon.isActive(),
            coupon.isExpired()
        );
    }
}
