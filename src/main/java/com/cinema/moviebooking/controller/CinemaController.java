package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.cinema.CinemaCreateRequest;
import com.cinema.moviebooking.dto.cinema.CinemaCreateResponse;
import com.cinema.moviebooking.service.CinemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * - 회원가입 성공 시 201(CREATED) 반환
     */
    @PostMapping
    public ResponseEntity<?> createCinema(@Valid @RequestBody CinemaCreateRequest req) {
        CinemaCreateResponse res = cinemaService.createCinema(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(res, "영화관 등록이 완료되었습니다."));
    }
}
