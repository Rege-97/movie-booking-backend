package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.theater.TheaterCreateRequest;
import com.cinema.moviebooking.dto.theater.TheaterCreateResponse;
import com.cinema.moviebooking.dto.theater.TheaterUpdateRequest;
import com.cinema.moviebooking.dto.theater.TheaterUpdateResponse;
import com.cinema.moviebooking.service.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 상영관 관련 요청을 처리하는 컨트롤러
 * (상영관 등록, 조회, 수정, 삭제 등)
 */
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
     * - 수정 성공 시 200(OK) 반환
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTheater(@PathVariable Long id, @Valid @RequestBody TheaterUpdateRequest req) {
        TheaterUpdateResponse res = theaterService.updateTheater(id, req);
        return ResponseEntity.ok().body(ApiResponse.success(res, "상영관 수정이 완료되었습니다."));
    }
}
