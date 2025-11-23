package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.theater.TheaterCreateRequest;
import com.cinema.moviebooking.dto.theater.TheaterCreateResponse;
import com.cinema.moviebooking.dto.theater.TheaterUpdateRequest;
import com.cinema.moviebooking.dto.theater.TheaterUpdateResponse;
import com.cinema.moviebooking.service.TheaterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 상영관 관련 요청을 처리하는 컨트롤러
 * (상영관 등록, 수정, 삭제 등)
 */
@Tag(name = "Theater", description = "상영관(스크린) 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/theaters")
public class TheaterController {

    private final TheaterService theaterService;

    /**
     * 상영관 등록 처리
     * - 요청값 검증(@Valid)
     * - 관리자 권한(ROLE_ADMIN) 필요
     * - 등록 성공 시 201(CREATED) 반환
     */
    @Operation(summary = "상영관 등록", description = "관리자 권한으로 상영관을 등록하고 좌석을 자동 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createTheater(@Valid @RequestBody TheaterCreateRequest req) {
        TheaterCreateResponse res = theaterService.createTheater(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(res, "상영관 등록이 완료되었습니다."));
    }

    /**
     * 상영관 수정 요청 처리
     * - PathVariable로 상영관 ID 전달
     * - 요청값 검증(@Valid)
     * - 관리자 권한(ROLE_ADMIN) 필요
     * - 수정 성공 시 200(OK) 반환
     */
    @Operation(summary = "상영관 수정", description = "관리자 권한으로 상영관 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTheater(@PathVariable Long id, @Valid @RequestBody TheaterUpdateRequest req) {
        TheaterUpdateResponse res = theaterService.updateTheater(id, req);
        return ResponseEntity.ok().body(ApiResponse.success(res, "상영관 수정이 완료되었습니다."));
    }

    /**
     * 상영관 삭제 요청 처리
     * - PathVariable로 ID 전달
     * - 관리자 권한(ROLE_ADMIN) 필요
     * - 삭제 성공 시 204(NO_CONTENT) 반환
     */
    @Operation(summary = "상영관 삭제", description = "관리자 권한으로 상영관을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }
}
