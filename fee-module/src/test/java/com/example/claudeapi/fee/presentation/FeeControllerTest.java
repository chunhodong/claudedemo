package com.example.claudeapi.fee.presentation;

import com.example.claudeapi.fee.application.FeeRequest;
import com.example.claudeapi.fee.application.FeeResponse;
import com.example.claudeapi.fee.domain.FeeUnit;
import com.example.claudeapi.fee.infrastructure.FeeJpaEntity;
import com.example.claudeapi.fee.infrastructure.FeeJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("FeeController 테스트")
class FeeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FeeJpaRepository feeJpaRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        feeJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("GET /api/fees 는")
    class Describe_getAll {

        @Nested
        @DisplayName("수수료가 존재하면")
        class Context_with_fees {

            @Test
            @DisplayName("전체 수수료 목록을 반환한다")
            void it_returns_all_fees() {
                // given
                FeeJpaEntity fee1 = new FeeJpaEntity(null, 1L, 1000L, FeeUnit.WON);
                feeJpaRepository.save(fee1);

                // when & then
                webTestClient.get().uri("/api/fees")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(FeeResponse.class)
                        .hasSize(1)
                        .value(fees -> {
                            assertThat(fees.get(0).getProductNumber()).isEqualTo(1L);
                        });
            }
        }
    }

    @Nested
    @DisplayName("POST /api/fees 는")
    class Describe_create {

        @Nested
        @DisplayName("유효한 수수료 정보로 생성하면")
        class Context_with_valid_request {

            @Test
            @DisplayName("새 수수료를 생성하고 반환한다")
            void it_creates_fee() {
                // given
                FeeRequest request = new FeeRequest(1L, 1000L, FeeUnit.WON);

                // when & then
                webTestClient.post().uri("/api/fees")
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody()
                        .jsonPath("$.productNumber").isEqualTo(1)
                        .jsonPath("$.feeValue").isEqualTo(1000)
                        .jsonPath("$.feeUnit").isEqualTo("WON")
                        .jsonPath("$.id").isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("PUT /api/fees/{id} 는")
    class Describe_update {

        @Nested
        @DisplayName("존재하는 수수료를 수정하면")
        class Context_with_existing_fee {

            @Test
            @DisplayName("수정된 수수료를 반환한다")
            void it_updates_fee() {
                // given
                FeeJpaEntity existing = new FeeJpaEntity(null, 1L, 1000L, FeeUnit.WON);
                FeeJpaEntity saved = feeJpaRepository.save(existing);

                FeeRequest request = new FeeRequest(2L, 2000L, FeeUnit.PERCENT);

                // when & then
                webTestClient.put().uri("/api/fees/" + saved.getId())
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.productNumber").isEqualTo(2)
                        .jsonPath("$.feeValue").isEqualTo(2000)
                        .jsonPath("$.feeUnit").isEqualTo("PERCENT");
            }
        }
    }

    @Nested
    @DisplayName("DELETE /api/fees/{id} 는")
    class Describe_delete {

        @Nested
        @DisplayName("존재하는 수수료를 삭제하면")
        class Context_with_existing_fee {

            @Test
            @DisplayName("204 No Content를 반환한다")
            void it_deletes_fee() {
                // given
                FeeJpaEntity existing = new FeeJpaEntity(null, 1L, 1000L, FeeUnit.WON);
                FeeJpaEntity saved = feeJpaRepository.save(existing);

                // when & then
                webTestClient.delete().uri("/api/fees/" + saved.getId())
                        .exchange()
                        .expectStatus().isNoContent();

                assertThat(feeJpaRepository.findById(saved.getId())).isEmpty();
            }
        }
    }
}
