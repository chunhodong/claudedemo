package com.example.claudedemo.product.application;

import com.example.claudedemo.product.domain.DuplicateSellerIdException;
import com.example.claudedemo.product.domain.Product;
import com.example.claudedemo.product.domain.ProductNotFoundException;
import com.example.claudedemo.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 테스트")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    @DisplayName("findAll 메소드는")
    class Describe_findAll {

        @Nested
        @DisplayName("상품이 존재하면")
        class Context_with_products {

            @Test
            @DisplayName("전체 상품 목록을 반환한다")
            void it_returns_all_products() {
                // given
                Product product1 = new Product(1L, "홍길동", "seller001", 1L);
                Product product2 = new Product(2L, "김철수", "seller002", 2L);
                given(productRepository.findAll()).willReturn(Arrays.asList(product1, product2));

                // when
                List<ProductResponse> result = productService.findAll();

                // then
                assertThat(result).hasSize(2);
                assertThat(result.get(0).getName()).isEqualTo("홍길동");
                assertThat(result.get(1).getName()).isEqualTo("김철수");
            }
        }
    }

    @Nested
    @DisplayName("findById 메소드는")
    class Describe_findById {

        @Nested
        @DisplayName("존재하는 ID로 조회하면")
        class Context_with_valid_id {

            @Test
            @DisplayName("해당 상품을 반환한다")
            void it_returns_product() {
                // given
                Product product = new Product(1L, "홍길동", "seller001", 1L);
                given(productRepository.findById(1L)).willReturn(Optional.of(product));

                // when
                ProductResponse result = productService.findById(1L);

                // then
                assertThat(result.getName()).isEqualTo("홍길동");
                assertThat(result.getSellerId()).isEqualTo("seller001");
            }
        }

        @Nested
        @DisplayName("존재하지 않는 ID로 조회하면")
        class Context_with_invalid_id {

            @Test
            @DisplayName("ProductNotFoundException을 던진다")
            void it_throws_exception() {
                // given
                given(productRepository.findById(999L)).willReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> productService.findById(999L))
                        .isInstanceOf(ProductNotFoundException.class)
                        .hasMessageContaining("Product not found");
            }
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("중복되지 않은 판매자 ID로 생성하면")
        class Context_with_unique_sellerId {

            @Test
            @DisplayName("새 상품을 생성하고 반환한다")
            void it_creates_product() {
                // given
                ProductRequest request = new ProductRequest(null, "홍길동", "seller001", 1L);
                Product savedProduct = new Product(1L, "홍길동", "seller001", 1L);
                given(productRepository.existsBySellerId("seller001")).willReturn(false);
                given(productRepository.save(any(Product.class))).willReturn(savedProduct);

                // when
                ProductResponse result = productService.create(request);

                // then
                assertThat(result.getId()).isEqualTo(1L);
                assertThat(result.getName()).isEqualTo("홍길동");
            }
        }

        @Nested
        @DisplayName("중복된 판매자 ID로 생성하면")
        class Context_with_duplicate_sellerId {

            @Test
            @DisplayName("DuplicateSellerIdException을 던진다")
            void it_throws_exception() {
                // given
                ProductRequest request = new ProductRequest(null, "홍길동", "seller001", 1L);
                given(productRepository.existsBySellerId("seller001")).willReturn(true);

                // when & then
                assertThatThrownBy(() -> productService.create(request))
                        .isInstanceOf(DuplicateSellerIdException.class)
                        .hasMessageContaining("Seller ID already exists");
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {

        @Nested
        @DisplayName("존재하는 상품을 수정하면")
        class Context_with_valid_product {

            @Test
            @DisplayName("수정된 상품 정보를 반환한다")
            void it_updates_product() {
                // given
                Product existingProduct = new Product(1L, "홍길동", "seller001", 1L);
                Product updatedProduct = new Product(1L, "김철수", "seller002", 2L);
                given(productRepository.findById(1L)).willReturn(Optional.of(existingProduct));
                given(productRepository.save(any(Product.class))).willReturn(updatedProduct);

                ProductRequest request = new ProductRequest(null, "김철수", "seller002", 2L);

                // when
                ProductResponse result = productService.update(1L, request);

                // then
                assertThat(result.getName()).isEqualTo("김철수");
                assertThat(result.getSellerId()).isEqualTo("seller002");
                assertThat(result.getCategoryNumber()).isEqualTo(2L);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("ID로 삭제하면")
        class Context_with_id {

            @Test
            @DisplayName("해당 상품을 삭제한다")
            void it_deletes_product() {
                // given
                Long productId = 1L;

                // when
                productService.delete(productId);

                // then
                then(productRepository).should().deleteById(productId);
            }
        }
    }
}
