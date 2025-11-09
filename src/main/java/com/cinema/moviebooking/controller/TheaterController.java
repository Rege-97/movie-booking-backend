package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.theater.TheaterCreateRequest;
import com.cinema.moviebooking.dto.theater.TheaterCreateResponse;
import com.cinema.moviebooking.service.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
