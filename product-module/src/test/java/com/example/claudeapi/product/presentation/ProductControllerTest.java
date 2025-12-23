package com.example.claudeapi.product.presentation;

import com.example.claudeapi.product.application.ProductRequest;
import com.example.claudeapi.product.application.ProductResponse;
import com.example.claudeapi.product.infrastructure.ProductJpaEntity;
import com.example.claudeapi.product.infrastructure.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    classes = com.example.claudedemo.product.TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("ProductController 테스트")
class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
        productJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("GET /api/products 는")
    class Describe_getAll {

        @Nested
        @DisplayName("상품이 존재하면")
        class Context_with_products {

            @Test
            @DisplayName("전체 상품 목록을 반환한다")
            void it_returns_all_products() {
                // given
                productJpaRepository.save(new ProductJpaEntity(null, "홍길동", "seller001", 1L));
                productJpaRepository.save(new ProductJpaEntity(null, "김철수", "seller002", 2L));

                // when & then
                webTestClient.get().uri("/api/products")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(ProductResponse.class).hasSize(2);
            }
        }
    }

    @Nested
    @DisplayName("GET /api/products/{id} 는")
    class Describe_getById {

        @Nested
        @DisplayName("존재하는 상품 ID로 조회하면")
        class Context_with_valid_id {

            @Test
            @DisplayName("해당 상품 정보를 반환한다")
            void it_returns_product() {
                // given
                ProductJpaEntity saved = productJpaRepository.save(new ProductJpaEntity(null, "홍길동", "seller001", 1L));

                // when & then
                webTestClient.get().uri("/api/products/" + saved.getId())
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.name").isEqualTo("홍길동")
                        .jsonPath("$.sellerId").isEqualTo("seller001");
            }
        }
    }

    @Nested
    @DisplayName("POST /api/products 는")
    class Describe_create {

        @Nested
        @DisplayName("유효한 상품 정보로 생성하면")
        class Context_with_valid_request {

            @Test
            @DisplayName("새 상품을 생성하고 반환한다")
            void it_creates_product() {
                // given
                ProductRequest request = new ProductRequest(null, "홍길동", "seller001", 1L);

                // when & then
                webTestClient.post().uri("/api/products")
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody()
                        .jsonPath("$.name").isEqualTo("홍길동")
                        .jsonPath("$.id").isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("PUT /api/products/{id} 는")
    class Describe_update {

        @Nested
        @DisplayName("존재하는 상품을 수정하면")
        class Context_with_valid_product {

            @Test
            @DisplayName("수정된 상품 정보를 반환한다")
            void it_updates_product() {
                // given
                ProductJpaEntity saved = productJpaRepository.save(new ProductJpaEntity(null, "홍길동", "seller001", 1L));
                ProductRequest request = new ProductRequest(null, "김철수", "seller002", 2L);

                // when & then
                webTestClient.put().uri("/api/products/" + saved.getId())
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.name").isEqualTo("김철수");
            }
        }
    }

    @Nested
    @DisplayName("DELETE /api/products/{id} 는")
    class Describe_delete {

        @Nested
        @DisplayName("존재하는 상품을 삭제하면")
        class Context_with_valid_id {

            @Test
            @DisplayName("상품을 삭제하고 204를 반환한다")
            void it_deletes_product() {
                // given
                ProductJpaEntity saved = productJpaRepository.save(new ProductJpaEntity(null, "홍길동", "seller001", 1L));

                // when & then
                webTestClient.delete().uri("/api/products/" + saved.getId())
                        .exchange()
                        .expectStatus().isNoContent();
            }
        }
    }
}
