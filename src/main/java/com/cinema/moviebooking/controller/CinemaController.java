package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.cinema.*;
import com.cinema.moviebooking.service.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 영화관 관련 요청을 처리하는 컨트롤러
 * (영화관 등록, 조회, 수정, 삭제 등)
 */
@Tag(name = "Cinema", description = "영화관 관리 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    /**
     * 영화관 등록 처리
     * - 요청값 검증(@Valid)
     * - 관리자 권한(ROLE_ADMIN) 필요
     * - 등록 성공 시 201(CREATED) 반환
     */
    @Operation(summary = "영화관 등록", description = "관리자 권한으로 새로운 영화관을 등록합니다.")
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
    @Operation(summary = "영화관 목록 조회", description = "검색어(keyword) 및 지역(region)으로 영화관을 조회합니다. (커서 기반 페이지네이션)")
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
    @Operation(summary = "영화관 상세 조회", description = "ID로 특정 영화관의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCinema(@PathVariable Long id) {
        CinemaResponse res = cinemaService.getCinemaById(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "영화관 상세 조회 성공"));
    }

    /**
     * 영화관 정보 수정 요청 처리
     * - PathVariable로 ID 전달
     * - 일부 필드만 선택적으로 수정 가능
     * - 관리자 권한(ROLE_ADMIN) 필요
     * - 수정 성공 시 200(OK) 반환
     */
    @Operation(summary = "영화관 정보 수정", description = "관리자 권한으로 영화관 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCinema(@PathVariable Long id, @Valid @RequestBody CinemaUpdateRequest req) {
        CinemaResponse res = cinemaService.updateCinema(id, req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "영화관 수정에 성공했습니다."));
    }

    /**
     * 영화관 삭제 요청 처리
     * - PathVariable로 ID 전달
     * - 관리자 권한(ROLE_ADMIN) 필요
     * - 삭제 성공 시 204(NO_CONTENT) 반환
     */
    @Operation(summary = "영화관 삭제", description = "관리자 권한으로 영화관을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCinema(@PathVariable Long id) {
        cinemaService.deleteCinema(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 영화관별 상영관 목록 조회 처리
     * - PathVariable로 ID 전달
     * - 조회 성공 시 200(OK) 반환
     */
    @Operation(summary = "영화관별 상영관 목록 조회", description = "특정 영화관에 소속된 상영관 목록을 조회합니다.")
    @GetMapping("/{id}/theaters")
    public ResponseEntity<?> getTheaters(@PathVariable Long id) {
        TheaterListResponse res = cinemaService.getTheatersByCinemaId(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "상영관 목록 조회 성공"));
    }

    /**
     * 영화관별 상영 스케줄 조회 요청 처리
     * - PathVariable로 영화관 ID 전달
     * - 상영일(screeningDate) 필터 적용
     * - 조회 성공 시 200(OK) 반환
     */
    @Operation(summary = "영화관별 상영 스케줄 조회", description = "특정 영화관의 날짜별 상영 스케줄을 조회합니다.")
    @GetMapping("/{id}/screenings")
    public ResponseEntity<?> getScreenings(@PathVariable Long id,
                                           @FutureOrPresent(message = "상영일은 오늘 날짜 이후여야 합니다.")
                                           @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().toString()}")
                                           LocalDate screeningDate) {
        CinemaScreeningResponse res = cinemaService.getCinemaScreening(id, screeningDate);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "상영스케줄 조회 성공"));
    }
}
