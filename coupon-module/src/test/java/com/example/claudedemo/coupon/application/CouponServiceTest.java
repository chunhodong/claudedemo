package com.example.claudedemo.coupon.application;

import com.example.claudedemo.coupon.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CouponService 테스트")
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("중복되지 않은 쿠폰 코드로 생성하면")
        class Context_with_unique_code {

            @Test
            @DisplayName("새 쿠폰을 생성하고 반환한다")
            void it_creates_coupon() {
                // given
                CouponRequest request = new CouponRequest(
                    "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );

                Coupon coupon = Coupon.create(
                    request.getCode(),
                    request.getDiscountRate(),
                    request.getMaxDiscountAmount(),
                    request.getExpiryDate()
                );

                Coupon savedCoupon = new Coupon(
                    1L,
                    coupon.getCode(),
                    coupon.getDiscountRate(),
                    coupon.getMaxDiscountAmount(),
                    coupon.getExpiryDate(),
                    coupon.isActive()
                );

                when(couponRepository.existsByCode(request.getCode())).thenReturn(false);
                when(couponRepository.save(any(Coupon.class))).thenReturn(savedCoupon);

                // when
                CouponResponse response = couponService.create(request);

                // then
                assertThat(response.getId()).isEqualTo(1L);
                assertThat(response.getCode()).isEqualTo("SUMMER2024");
                verify(couponRepository).existsByCode("SUMMER2024");
                verify(couponRepository).save(any(Coupon.class));
            }
        }

        @Nested
        @DisplayName("중복된 쿠폰 코드로 생성하면")
        class Context_with_duplicate_code {

            @Test
            @DisplayName("DuplicateCouponCodeException을 던진다")
            void it_throws_exception() {
                // given
                CouponRequest request = new CouponRequest(
                    "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );

                when(couponRepository.existsByCode("SUMMER2024")).thenReturn(true);

                // when & then
                assertThatThrownBy(() -> couponService.create(request))
                        .isInstanceOf(DuplicateCouponCodeException.class)
                        .hasMessageContaining("SUMMER2024");
            }
        }

        @Nested
        @DisplayName("과거 만료일로 생성하면")
        class Context_with_past_expiry_date {

            @Test
            @DisplayName("IllegalArgumentException을 던진다")
            void it_throws_exception() {
                // given
                CouponRequest request = new CouponRequest(
                    "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().minusDays(1), true
                );

                when(couponRepository.existsByCode("SUMMER2024")).thenReturn(false);

                // when & then
                assertThatThrownBy(() -> couponService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("Expiry date must be in the future");
            }
        }
    }

    @Nested
    @DisplayName("findAll 메소드는")
    class Describe_findAll {

        @Nested
        @DisplayName("쿠폰이 존재하면")
        class Context_with_coupons {

            @Test
            @DisplayName("전체 쿠폰 목록을 반환한다")
            void it_returns_all_coupons() {
                // given
                Coupon coupon1 = new Coupon(
                    1L, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );
                Coupon coupon2 = new Coupon(
                    2L, "WINTER2024", 15, 5000L,
                    LocalDateTime.now().plusDays(60), true
                );

                when(couponRepository.findAll()).thenReturn(List.of(coupon1, coupon2));

                // when
                List<CouponResponse> responses = couponService.findAll();

                // then
                assertThat(responses).hasSize(2);
                assertThat(responses.get(0).getCode()).isEqualTo("SUMMER2024");
                assertThat(responses.get(1).getCode()).isEqualTo("WINTER2024");
            }
        }
    }

    @Nested
    @DisplayName("findById 메소드는")
    class Describe_findById {

        @Nested
        @DisplayName("존재하는 ID로 조회하면")
        class Context_with_existing_id {

            @Test
            @DisplayName("해당 쿠폰을 반환한다")
            void it_returns_coupon() {
                // given
                Coupon coupon = new Coupon(
                    1L, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );

                when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));

                // when
                CouponResponse response = couponService.findById(1L);

                // then
                assertThat(response.getId()).isEqualTo(1L);
                assertThat(response.getCode()).isEqualTo("SUMMER2024");
            }
        }

        @Nested
        @DisplayName("존재하지 않는 ID로 조회하면")
        class Context_with_non_existing_id {

            @Test
            @DisplayName("CouponNotFoundException을 던진다")
            void it_throws_exception() {
                // given
                when(couponRepository.findById(999L)).thenReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> couponService.findById(999L))
                        .isInstanceOf(CouponNotFoundException.class)
                        .hasMessageContaining("999");
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("유효한 정보로 수정하면")
        class Context_with_valid_request {

            @Test
            @DisplayName("쿠폰을 수정하고 반환한다")
            void it_updates_coupon() {
                // given
                Coupon existingCoupon = new Coupon(
                    1L, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );

                CouponRequest request = new CouponRequest(
                    "SUMMER2024", 25, 15000L,
                    LocalDateTime.now().plusDays(45), false
                );

                when(couponRepository.findById(1L)).thenReturn(Optional.of(existingCoupon));
                when(couponRepository.existsByCodeAndIdNot("SUMMER2024", 1L)).thenReturn(false);
                when(couponRepository.save(any(Coupon.class))).thenReturn(existingCoupon);

                // when
                CouponResponse response = couponService.update(1L, request);

                // then
                assertThat(response.getDiscountRate()).isEqualTo(25);
                assertThat(response.getMaxDiscountAmount()).isEqualTo(15000L);
                verify(couponRepository).save(any(Coupon.class));
            }
        }

        @Nested
        @DisplayName("다른 쿠폰의 코드와 중복되면")
        class Context_with_duplicate_code {

            @Test
            @DisplayName("DuplicateCouponCodeException을 던진다")
            void it_throws_exception() {
                // given
                Coupon existingCoupon = new Coupon(
                    1L, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );

                CouponRequest request = new CouponRequest(
                    "WINTER2024", 25, 15000L,
                    LocalDateTime.now().plusDays(45), true
                );

                when(couponRepository.findById(1L)).thenReturn(Optional.of(existingCoupon));
                when(couponRepository.existsByCodeAndIdNot("WINTER2024", 1L)).thenReturn(true);

                // when & then
                assertThatThrownBy(() -> couponService.update(1L, request))
                        .isInstanceOf(DuplicateCouponCodeException.class)
                        .hasMessageContaining("WINTER2024");
            }
        }
    }

    @Nested
    @DisplayName("validateCoupon 메소드는")
    class Describe_validateCoupon {

        @Nested
        @DisplayName("유효한 쿠폰이면")
        class Context_with_valid_coupon {

            @Test
            @DisplayName("쿠폰 정보를 반환한다")
            void it_returns_coupon() {
                // given
                Coupon coupon = new Coupon(
                    1L, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );

                when(couponRepository.findByCode("SUMMER2024")).thenReturn(Optional.of(coupon));

                // when
                CouponResponse response = couponService.validateCoupon("SUMMER2024");

                // then
                assertThat(response.getCode()).isEqualTo("SUMMER2024");
                assertThat(response.getIsActive()).isTrue();
                assertThat(response.getIsExpired()).isFalse();
            }
        }

        @Nested
        @DisplayName("만료된 쿠폰이면")
        class Context_with_expired_coupon {

            @Test
            @DisplayName("ExpiredCouponException을 던진다")
            void it_throws_exception() {
                // given
                Coupon expiredCoupon = new Coupon(
                    1L, "EXPIRED2023", 20, 10000L,
                    LocalDateTime.now().minusDays(1), true
                );

                when(couponRepository.findByCode("EXPIRED2023")).thenReturn(Optional.of(expiredCoupon));

                // when & then
                assertThatThrownBy(() -> couponService.validateCoupon("EXPIRED2023"))
                        .isInstanceOf(ExpiredCouponException.class)
                        .hasMessageContaining("EXPIRED2023");
            }
        }

        @Nested
        @DisplayName("비활성화된 쿠폰이면")
        class Context_with_inactive_coupon {

            @Test
            @DisplayName("ExpiredCouponException을 던진다")
            void it_throws_exception() {
                // given
                Coupon inactiveCoupon = new Coupon(
                    1L, "INACTIVE2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), false
                );

                when(couponRepository.findByCode("INACTIVE2024")).thenReturn(Optional.of(inactiveCoupon));

                // when & then
                assertThatThrownBy(() -> couponService.validateCoupon("INACTIVE2024"))
                        .isInstanceOf(ExpiredCouponException.class)
                        .hasMessageContaining("INACTIVE2024");
            }
        }
    }

    @Nested
    @DisplayName("deactivate 메소드는")
    class Describe_deactivate {

        @Nested
        @DisplayName("존재하는 쿠폰을 비활성화하면")
        class Context_with_existing_coupon {

            @Test
            @DisplayName("쿠폰을 비활성화한다")
            void it_deactivates_coupon() {
                // given
                Coupon coupon = new Coupon(
                    1L, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );

                when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));
                when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

                // when
                couponService.deactivate(1L);

                // then
                verify(couponRepository).findById(1L);
                verify(couponRepository).save(any(Coupon.class));
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("존재하는 쿠폰을 삭제하면")
        class Context_with_existing_coupon {

            @Test
            @DisplayName("쿠폰을 삭제한다")
            void it_deletes_coupon() {
                // given
                Coupon coupon = new Coupon(
                    1L, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );

                when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));

                // when
                couponService.delete(1L);

                // then
                verify(couponRepository).findById(1L);
                verify(couponRepository).deleteById(1L);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 쿠폰을 삭제하면")
        class Context_with_non_existing_coupon {

            @Test
            @DisplayName("CouponNotFoundException을 던진다")
            void it_throws_exception() {
                // given
                when(couponRepository.findById(999L)).thenReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> couponService.delete(999L))
                        .isInstanceOf(CouponNotFoundException.class)
                        .hasMessageContaining("999");
            }
        }
    }
}
