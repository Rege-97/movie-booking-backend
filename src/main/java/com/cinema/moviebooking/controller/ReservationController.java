package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.reservation.ReservationCreateRequest;
import com.cinema.moviebooking.dto.reservation.ReservationCreateResponse;
import com.cinema.moviebooking.entity.Member;
import com.cinema.moviebooking.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 영화 예매 요청을 처리하는 컨트롤러
 * (영화 예매, 취소 등)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 영화 예매 처리
     * - 요청값 검증(@Valid)
     * - 로그인 필요
     * - 등록 성공 시 201(CREATED) 반환
     */
    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationCreateRequest req,
                                               @AuthenticationPrincipal Member member) {
        ReservationCreateResponse res = reservationService.createReservation(member, req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(res, "영화 예매가 완료되었습니다."));
    }
}
