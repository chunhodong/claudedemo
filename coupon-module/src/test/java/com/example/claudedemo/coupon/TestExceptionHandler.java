package com.example.claudedemo.coupon;

import com.example.claudedemo.coupon.domain.CouponNotFoundException;
import com.example.claudedemo.coupon.domain.DuplicateCouponCodeException;
import com.example.claudedemo.coupon.domain.ExpiredCouponException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class TestExceptionHandler {

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error: " + e.getMessage()));
    }
}
