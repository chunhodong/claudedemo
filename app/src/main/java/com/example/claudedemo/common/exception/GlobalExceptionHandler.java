package com.example.claudedemo.common.exception;

import com.example.claudedemo.product.domain.DuplicateSellerIdException;
import com.example.claudedemo.product.domain.ProductNotFoundException;
import com.example.claudedemo.coupon.domain.CouponNotFoundException;
import com.example.claudedemo.coupon.domain.DuplicateCouponCodeException;
import com.example.claudedemo.coupon.domain.ExpiredCouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Product 예외 핸들러
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(DuplicateSellerIdException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateSellerId(DuplicateSellerIdException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
    }

    // Coupon 예외 핸들러
    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCouponNotFound(CouponNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(DuplicateCouponCodeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateCouponCode(DuplicateCouponCodeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(ExpiredCouponException.class)
    public ResponseEntity<Map<String, String>> handleExpiredCoupon(ExpiredCouponException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }

    // 공통 예외 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        log.error("Unhandled exception occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error: " + e.getMessage()));
    }
}
