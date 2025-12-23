package com.example.claudeapi.fee.application;

import com.example.claudeapi.fee.domain.FeeUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "수수료 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeeRequest {

    @Schema(description = "상품 번호", example = "1", required = true)
    private Long productNumber;

    @Schema(description = "수수료 값", example = "1000", required = true)
    private Long feeValue;

    @Schema(description = "수수료 단위 (PERCENT 또는 WON)", example = "WON", required = true)
    private FeeUnit feeUnit;
}
