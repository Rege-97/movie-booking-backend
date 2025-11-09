package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.cinema.CinemaCreateRequest;
import com.cinema.moviebooking.dto.cinema.CinemaCreateResponse;
import com.cinema.moviebooking.dto.cinema.CinemaCursorResponse;
import com.cinema.moviebooking.dto.cinema.CinemaResponse;
import com.cinema.moviebooking.service.CinemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 영화관 관련 요청을 처리하는 컨트롤러
 * (영화관 등록, 조회, 수정, 삭제 등)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    /**
     * 영화관 등록 처리
     * - 요청값 검증(@Valid)
     * - 등록 성공 시 201(CREATED) 반환
     */
    @PostMapping
    public ResponseEntity<?> createCinema(@Valid @RequestBody CinemaCreateRequest req) {
        CinemaCreateResponse res = cinemaService.createCinema(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(res, "영화관 등록이 완료되었습니다."));
    }

    /**
     * 영화관 목록 조회 요청 처리
     * - 검색어(keyword) 및 지역(region) 필터 적용
     * - 커서 기반 페이지네이션 지원 (lastId, size)
     * - 조회 성공 시 200(OK) 상태 코드 반환
     */
    @GetMapping
    public ResponseEntity<?> getCinemas(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {
        CinemaCursorResponse res = cinemaService.getCinemasByCursor(keyword, region, lastId, size);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "영화관 목록 조회 성공"));
    }

    /**
     * 영화관 상세 조회 요청 처리
     * - PathVariable로 ID 전달
     * - 조회 성공 시 200(OK) 상태 코드 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCinema(@PathVariable Long id) {
        CinemaResponse res = cinemaService.getCinemaById(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "영화관 상세 조회 성공"));
    }
}
