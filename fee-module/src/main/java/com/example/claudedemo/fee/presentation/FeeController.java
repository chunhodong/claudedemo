package com.example.claudedemo.fee.presentation;

import com.example.claudedemo.fee.application.FeeRequest;
import com.example.claudedemo.fee.application.FeeResponse;
import com.example.claudedemo.fee.application.FeeService;
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

@Tag(name = "수수료 관리", description = "수수료 CRUD API")
@RestController
@RequestMapping("/api/fees")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;

    @Operation(summary = "전체 수수료 조회", description = "등록된 모든 수수료를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<FeeResponse>> getAll() {
        return ResponseEntity.ok(feeService.findAll());
    }

    @Operation(summary = "수수료 단건 조회", description = "ID로 특정 수수료를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "수수료를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FeeResponse> getById(
            @Parameter(description = "수수료 ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(feeService.findById(id));
    }

    @Operation(summary = "수수료 생성", description = "새로운 수수료를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공")
    })
    @PostMapping
    public ResponseEntity<FeeResponse> create(@RequestBody FeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feeService.create(request));
    }

    @Operation(summary = "수수료 수정", description = "기존 수수료 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "수수료를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FeeResponse> update(
            @Parameter(description = "수수료 ID", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody FeeRequest request) {
        return ResponseEntity.ok(feeService.update(id, request));
    }

    @Operation(summary = "수수료 삭제", description = "ID로 수수료를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "수수료를 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "수수료 ID", required = true, example = "1")
            @PathVariable Long id) {
        feeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
