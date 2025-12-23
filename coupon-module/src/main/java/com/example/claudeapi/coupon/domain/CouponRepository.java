package com.example.claudeapi.coupon.domain;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Coupon save(Coupon coupon);
    Optional<Coupon> findById(Long id);
    Optional<Coupon> findByCode(String code);
    List<Coupon> findAll();
    void deleteById(Long id);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
