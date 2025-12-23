package com.example.claudeapi.coupon.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponJpaRepository extends JpaRepository<CouponJpaEntity, Long> {
    Optional<CouponJpaEntity> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
