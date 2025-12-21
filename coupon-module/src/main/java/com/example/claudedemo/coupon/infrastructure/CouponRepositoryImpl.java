package com.example.claudedemo.coupon.infrastructure;

import com.example.claudedemo.coupon.domain.Coupon;
import com.example.claudedemo.coupon.domain.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository jpaRepository;

    @Override
    public Coupon save(Coupon coupon) {
        CouponJpaEntity entity = CouponJpaEntity.from(coupon);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Coupon> findById(Long id) {
        return jpaRepository.findById(id).map(CouponJpaEntity::toDomain);
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        return jpaRepository.findByCode(code).map(CouponJpaEntity::toDomain);
    }

    @Override
    public List<Coupon> findAll() {
        return jpaRepository.findAll().stream()
                .map(CouponJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, Long id) {
        return jpaRepository.existsByCodeAndIdNot(code, id);
    }
}
