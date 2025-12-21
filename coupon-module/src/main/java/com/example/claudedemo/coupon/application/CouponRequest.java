package com.example.claudedemo.coupon.application;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "쿠폰 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequest {

    @Schema(description = "쿠폰 코드 (고유값)", example = "SUMMER2024", required = true)
    private String code;

    @Schema(description = "할인율 (1-100%)", example = "20", required = true)
    private Integer discountRate;

    @Schema(description = "최대 할인 금액", example = "10000")
    private Long maxDiscountAmount;

    @Schema(description = "만료일", example = "2024-12-31T23:59:59", required = true)
    private LocalDateTime expiryDate;

    @Schema(description = "활성화 상태", example = "true")
    private Boolean isActive;
}
