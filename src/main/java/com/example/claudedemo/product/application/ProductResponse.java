package com.example.claudedemo.product.application;

import com.example.claudedemo.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "상품 응답 DTO")
@Getter
@AllArgsConstructor
public class ProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품명", example = "노트북")
    private String name;

    @Schema(description = "판매자 ID", example = "seller001")
    private String sellerId;

    @Schema(description = "카테고리 번호", example = "1")
    private Long categoryNumber;

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getSellerId(), product.getCategoryNumber());
    }
}
