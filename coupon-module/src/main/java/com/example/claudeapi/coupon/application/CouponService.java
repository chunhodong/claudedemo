package com.example.claudeapi.coupon.application;

import com.example.claudeapi.coupon.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<CouponResponse> findAll() {
        return couponRepository.findAll().stream()
                .map(CouponResponse::from)
                .toList();
    }

    public CouponResponse findById(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));
        return CouponResponse.from(coupon);
    }

    public CouponResponse findByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new CouponNotFoundException(code));
        return CouponResponse.from(coupon);
    }

    @Transactional
    public CouponResponse create(CouponRequest request) {
        if (couponRepository.existsByCode(request.getCode())) {
            throw new DuplicateCouponCodeException(request.getCode());
        }

        if (request.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expiry date must be in the future");
        }

        Coupon coupon = Coupon.create(
            request.getCode(),
            request.getDiscountRate(),
            request.getMaxDiscountAmount(),
            request.getExpiryDate()
        );

        Coupon saved = couponRepository.save(coupon);
        return CouponResponse.from(saved);
    }

    @Transactional
    public CouponResponse update(Long id, CouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        if (couponRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new DuplicateCouponCodeException(request.getCode());
        }

        coupon.update(
            request.getCode(),
            request.getDiscountRate(),
            request.getMaxDiscountAmount(),
            request.getExpiryDate(),
            request.getIsActive()
        );

        Coupon updated = couponRepository.save(coupon);
        return CouponResponse.from(updated);
    }

    @Transactional
    public void deactivate(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));
        coupon.deactivate();
        couponRepository.save(coupon);
    }

    @Transactional
    public void delete(Long id) {
        if (!couponRepository.findById(id).isPresent()) {
            throw new CouponNotFoundException(id);
        }
        couponRepository.deleteById(id);
    }

    public CouponResponse validateCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new CouponNotFoundException(code));

        if (!coupon.isActive() || coupon.isExpired()) {
            throw new ExpiredCouponException(code);
        }

        return CouponResponse.from(coupon);
    }
}
