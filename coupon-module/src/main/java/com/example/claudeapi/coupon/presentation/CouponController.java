package com.example.claudeapi.coupon.presentation;

import com.example.claudeapi.coupon.application.CouponRequest;
import com.example.claudeapi.coupon.application.CouponResponse;
import com.example.claudeapi.coupon.application.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "쿠폰 관리", description = "쿠폰 CRUD API")
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "전체 쿠폰 조회", description = "등록된 모든 쿠폰을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAll() {
        return ResponseEntity.ok(couponService.findAll());
    }

    @Operation(summary = "쿠폰 단건 조회", description = "ID로 특정 쿠폰을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "쿠폰을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> getById(
            @Parameter(description = "쿠폰 ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(couponService.findById(id));
    }

    @Operation(summary = "쿠폰 코드로 조회", description = "쿠폰 코드로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "쿠폰을 찾을 수 없음")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<CouponResponse> getByCode(
            @Parameter(description = "쿠폰 코드", required = true, example = "SUMMER2024")
            @PathVariable String code) {
        return ResponseEntity.ok(couponService.findByCode(code));
    }

    @Operation(summary = "쿠폰 생성", description = "새로운 쿠폰을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "409", description = "쿠폰 코드 중복")
    })
    @PostMapping
    public ResponseEntity<CouponResponse> create(@RequestBody CouponRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(couponService.create(request));
    }

    @Operation(summary = "쿠폰 수정", description = "기존 쿠폰 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "쿠폰을 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "쿠폰 코드 중복")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CouponResponse> update(
            @Parameter(description = "쿠폰 ID", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody CouponRequest request) {
        return ResponseEntity.ok(couponService.update(id, request));
    }

    @Operation(summary = "쿠폰 비활성화", description = "쿠폰을 비활성화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비활성화 성공"),
            @ApiResponse(responseCode = "404", description = "쿠폰을 찾을 수 없음")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @Parameter(description = "쿠폰 ID", required = true, example = "1")
            @PathVariable Long id) {
        couponService.deactivate(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "쿠폰 삭제", description = "ID로 쿠폰을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "쿠폰을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "쿠폰 ID", required = true, example = "1")
            @PathVariable Long id) {
        couponService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "쿠폰 검증", description = "쿠폰 코드로 사용 가능 여부를 검증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 쿠폰"),
            @ApiResponse(responseCode = "400", description = "만료되거나 비활성화된 쿠폰"),
            @ApiResponse(responseCode = "404", description = "쿠폰을 찾을 수 없음")
    })
    @PostMapping("/validate")
    public ResponseEntity<CouponResponse> validateCoupon(
            @RequestBody Map<String, String> request) {
        String code = request.get("code");
        return ResponseEntity.ok(couponService.validateCoupon(code));
    }
}
