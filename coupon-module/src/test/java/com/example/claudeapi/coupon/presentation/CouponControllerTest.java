package com.example.claudeapi.coupon.presentation;

import com.example.claudeapi.coupon.application.CouponRequest;
import com.example.claudeapi.coupon.application.CouponResponse;
import com.example.claudeapi.coupon.infrastructure.CouponJpaEntity;
import com.example.claudeapi.coupon.infrastructure.CouponJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = com.example.claudeapi.coupon.TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("CouponController 테스트")
class CouponControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        couponJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("GET /api/coupons 는")
    class Describe_getAll {

        @Nested
        @DisplayName("쿠폰이 존재하면")
        class Context_with_coupons {

            @Test
            @DisplayName("전체 쿠폰 목록을 반환한다")
            void it_returns_all_coupons() {
                // given
                CouponJpaEntity coupon1 = new CouponJpaEntity(
                    null, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );
                CouponJpaEntity coupon2 = new CouponJpaEntity(
                    null, "WINTER2024", 15, 5000L,
                    LocalDateTime.now().plusDays(60), true
                );
                couponJpaRepository.save(coupon1);
                couponJpaRepository.save(coupon2);

                // when & then
                webTestClient.get().uri("/api/coupons")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(CouponResponse.class)
                        .hasSize(2)
                        .value(coupons -> {
                            assertThat(coupons.get(0).getCode()).isIn("SUMMER2024", "WINTER2024");
                            assertThat(coupons.get(1).getCode()).isIn("SUMMER2024", "WINTER2024");
                        });
            }
        }

        @Nested
        @DisplayName("쿠폰이 없으면")
        class Context_with_no_coupons {

            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returns_empty_list() {
                // when & then
                webTestClient.get().uri("/api/coupons")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(CouponResponse.class)
                        .hasSize(0);
            }
        }
    }

    @Nested
    @DisplayName("GET /api/coupons/{id} 는")
    class Describe_getById {

        @Nested
        @DisplayName("존재하는 ID로 조회하면")
        class Context_with_existing_id {

            @Test
            @DisplayName("해당 쿠폰을 반환한다")
            void it_returns_coupon() {
                // given
                CouponJpaEntity coupon = new CouponJpaEntity(
                    null, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );
                CouponJpaEntity saved = couponJpaRepository.save(coupon);

                // when & then
                webTestClient.get().uri("/api/coupons/{id}", saved.getId())
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.id").isEqualTo(saved.getId())
                        .jsonPath("$.code").isEqualTo("SUMMER2024")
                        .jsonPath("$.discountRate").isEqualTo(20);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 ID로 조회하면")
        class Context_with_non_existing_id {

            @Test
            @DisplayName("404 Not Found를 반환한다")
            void it_returns_not_found() {
                // when & then
                webTestClient.get().uri("/api/coupons/{id}", 999L)
                        .exchange()
                        .expectStatus().isNotFound();
            }
        }
    }

    @Nested
    @DisplayName("GET /api/coupons/code/{code} 는")
    class Describe_getByCode {

        @Nested
        @DisplayName("존재하는 코드로 조회하면")
        class Context_with_existing_code {

            @Test
            @DisplayName("해당 쿠폰을 반환한다")
            void it_returns_coupon() {
                // given
                CouponJpaEntity coupon = new CouponJpaEntity(
                    null, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );
                couponJpaRepository.save(coupon);

                // when & then
                webTestClient.get().uri("/api/coupons/code/{code}", "SUMMER2024")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.code").isEqualTo("SUMMER2024")
                        .jsonPath("$.discountRate").isEqualTo(20);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 코드로 조회하면")
        class Context_with_non_existing_code {

            @Test
            @DisplayName("404 Not Found를 반환한다")
            void it_returns_not_found() {
                // when & then
                webTestClient.get().uri("/api/coupons/code/{code}", "NONEXISTENT")
                        .exchange()
                        .expectStatus().isNotFound();
            }
        }
    }

    @Nested
    @DisplayName("POST /api/coupons 는")
    class Describe_create {

        @Nested
        @DisplayName("유효한 쿠폰 정보로 생성하면")
        class Context_with_valid_request {

            @Test
            @DisplayName("새 쿠폰을 생성하고 반환한다")
            void it_creates_coupon() {
                // given
                CouponRequest request = new CouponRequest(
                    "WINTER2024", 15, 5000L,
                    LocalDateTime.now().plusDays(60), true
                );

                // when & then
                webTestClient.post().uri("/api/coupons")
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody()
                        .jsonPath("$.code").isEqualTo("WINTER2024")
                        .jsonPath("$.discountRate").isEqualTo(15)
                        .jsonPath("$.maxDiscountAmount").isEqualTo(5000)
                        .jsonPath("$.id").isNotEmpty();
            }
        }

        @Nested
        @DisplayName("중복된 쿠폰 코드로 생성하면")
        class Context_with_duplicate_code {

            @Test
            @DisplayName("409 Conflict를 반환한다")
            void it_returns_conflict() {
                // given
                CouponJpaEntity existing = new CouponJpaEntity(
                    null, "DUPLICATE", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );
                couponJpaRepository.save(existing);

                CouponRequest request = new CouponRequest(
                    "DUPLICATE", 15, 5000L,
                    LocalDateTime.now().plusDays(60), true
                );

                // when & then
                webTestClient.post().uri("/api/coupons")
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isEqualTo(409);
            }
        }

        @Nested
        @DisplayName("과거 만료일로 생성하면")
        class Context_with_past_expiry_date {

            @Test
            @DisplayName("500 Internal Server Error를 반환한다")
            void it_returns_error() {
                // given
                CouponRequest request = new CouponRequest(
                    "PAST2023", 20, 10000L,
                    LocalDateTime.now().minusDays(1), true
                );

                // when & then
                webTestClient.post().uri("/api/coupons")
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().is5xxServerError();
            }
        }
    }

    @Nested
    @DisplayName("PUT /api/coupons/{id} 는")
    class Describe_update {

        @Nested
        @DisplayName("유효한 정보로 수정하면")
        class Context_with_valid_request {

            @Test
            @DisplayName("쿠폰을 수정하고 반환한다")
            void it_updates_coupon() {
                // given
                CouponJpaEntity existing = new CouponJpaEntity(
                    null, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );
                CouponJpaEntity saved = couponJpaRepository.save(existing);

                CouponRequest request = new CouponRequest(
                    "SUMMER2024", 25, 15000L,
                    LocalDateTime.now().plusDays(45), false
                );

                // when & then
                webTestClient.put().uri("/api/coupons/{id}", saved.getId())
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.id").isEqualTo(saved.getId())
                        .jsonPath("$.discountRate").isEqualTo(25)
                        .jsonPath("$.maxDiscountAmount").isEqualTo(15000);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 ID로 수정하면")
        class Context_with_non_existing_id {

            @Test
            @DisplayName("404 Not Found를 반환한다")
            void it_returns_not_found() {
                // given
                CouponRequest request = new CouponRequest(
                    "SUMMER2024", 25, 15000L,
                    LocalDateTime.now().plusDays(45), true
                );

                // when & then
                webTestClient.put().uri("/api/coupons/{id}", 999L)
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isNotFound();
            }
        }
    }

    @Nested
    @DisplayName("PATCH /api/coupons/{id}/deactivate 는")
    class Describe_deactivate {

        @Nested
        @DisplayName("존재하는 쿠폰을 비활성화하면")
        class Context_with_existing_coupon {

            @Test
            @DisplayName("쿠폰을 비활성화한다")
            void it_deactivates_coupon() {
                // given
                CouponJpaEntity coupon = new CouponJpaEntity(
                    null, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );
                CouponJpaEntity saved = couponJpaRepository.save(coupon);

                // when & then
                webTestClient.patch().uri("/api/coupons/{id}/deactivate", saved.getId())
                        .exchange()
                        .expectStatus().isOk();

                // verify
                webTestClient.get().uri("/api/coupons/{id}", saved.getId())
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.isActive").isEqualTo(false);
            }
        }
    }

    @Nested
    @DisplayName("DELETE /api/coupons/{id} 는")
    class Describe_delete {

        @Nested
        @DisplayName("존재하는 쿠폰을 삭제하면")
        class Context_with_existing_coupon {

            @Test
            @DisplayName("쿠폰을 삭제한다")
            void it_deletes_coupon() {
                // given
                CouponJpaEntity coupon = new CouponJpaEntity(
                    null, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );
                CouponJpaEntity saved = couponJpaRepository.save(coupon);

                // when & then
                webTestClient.delete().uri("/api/coupons/{id}", saved.getId())
                        .exchange()
                        .expectStatus().isNoContent();

                // verify
                webTestClient.get().uri("/api/coupons/{id}", saved.getId())
                        .exchange()
                        .expectStatus().isNotFound();
            }
        }

        @Nested
        @DisplayName("존재하지 않는 쿠폰을 삭제하면")
        class Context_with_non_existing_coupon {

            @Test
            @DisplayName("404 Not Found를 반환한다")
            void it_returns_not_found() {
                // when & then
                webTestClient.delete().uri("/api/coupons/{id}", 999L)
                        .exchange()
                        .expectStatus().isNotFound();
            }
        }
    }

    @Nested
    @DisplayName("POST /api/coupons/validate 는")
    class Describe_validateCoupon {

        @Nested
        @DisplayName("유효한 쿠폰 코드로 검증하면")
        class Context_with_valid_coupon {

            @Test
            @DisplayName("쿠폰 정보를 반환한다")
            void it_returns_coupon() {
                // given
                CouponJpaEntity coupon = new CouponJpaEntity(
                    null, "SUMMER2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), true
                );
                couponJpaRepository.save(coupon);

                // when & then
                webTestClient.post().uri("/api/coupons/validate")
                        .bodyValue(java.util.Map.of("code", "SUMMER2024"))
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.code").isEqualTo("SUMMER2024")
                        .jsonPath("$.isActive").isEqualTo(true)
                        .jsonPath("$.isExpired").isEqualTo(false);
            }
        }

        @Nested
        @DisplayName("만료된 쿠폰 코드로 검증하면")
        class Context_with_expired_coupon {

            @Test
            @DisplayName("400 Bad Request를 반환한다")
            void it_returns_bad_request() {
                // given
                CouponJpaEntity expiredCoupon = new CouponJpaEntity(
                    null, "EXPIRED2023", 20, 10000L,
                    LocalDateTime.now().minusDays(1), true
                );
                couponJpaRepository.save(expiredCoupon);

                // when & then
                webTestClient.post().uri("/api/coupons/validate")
                        .bodyValue(java.util.Map.of("code", "EXPIRED2023"))
                        .exchange()
                        .expectStatus().isBadRequest();
            }
        }

        @Nested
        @DisplayName("비활성화된 쿠폰 코드로 검증하면")
        class Context_with_inactive_coupon {

            @Test
            @DisplayName("400 Bad Request를 반환한다")
            void it_returns_bad_request() {
                // given
                CouponJpaEntity inactiveCoupon = new CouponJpaEntity(
                    null, "INACTIVE2024", 20, 10000L,
                    LocalDateTime.now().plusDays(30), false
                );
                couponJpaRepository.save(inactiveCoupon);

                // when & then
                webTestClient.post().uri("/api/coupons/validate")
                        .bodyValue(java.util.Map.of("code", "INACTIVE2024"))
                        .exchange()
                        .expectStatus().isBadRequest();
            }
        }

        @Nested
        @DisplayName("존재하지 않는 쿠폰 코드로 검증하면")
        class Context_with_non_existing_code {

            @Test
            @DisplayName("404 Not Found를 반환한다")
            void it_returns_not_found() {
                // when & then
                webTestClient.post().uri("/api/coupons/validate")
                        .bodyValue(java.util.Map.of("code", "NONEXISTENT"))
                        .exchange()
                        .expectStatus().isNotFound();
            }
        }
    }
}
