package com.example.claudeapi.fee.application;

import com.example.claudeapi.fee.domain.Fee;
import com.example.claudeapi.fee.domain.FeeUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "수수료 응답 DTO")
@Getter
@AllArgsConstructor
public class FeeResponse {

    @Schema(description = "수수료 ID", example = "1")
    private Long id;

    @Schema(description = "상품 번호", example = "1")
    private Long productNumber;

    @Schema(description = "수수료 값", example = "1000")
    private Long feeValue;

    @Schema(description = "수수료 단위", example = "WON")
    private FeeUnit feeUnit;

    public static FeeResponse from(Fee fee) {
        return new FeeResponse(
            fee.getId(),
            fee.getProductNumber(),
            fee.getFeeValue(),
            fee.getFeeUnit()
        );
    }
}
