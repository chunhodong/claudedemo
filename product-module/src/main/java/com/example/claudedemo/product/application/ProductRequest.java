package com.example.claudedemo.product.application;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "상품 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @Schema(description = "상품 ID (수정 시 사용, 생성 시 null)", example = "1")
    private Long id;

    @Schema(description = "상품명", example = "노트북", required = true)
    private String name;

    @Schema(description = "판매자 ID (고유값)", example = "seller001", required = true)
    private String sellerId;

    @Schema(description = "카테고리 번호", example = "1")
    private Long categoryNumber;
}
