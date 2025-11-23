package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.movie.*;
import com.cinema.moviebooking.entity.Rating;
import com.cinema.moviebooking.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 영화 관련 요청을 처리하는 컨트롤러
 * (영화 등록, 수정, 삭제 등)
 */
@Tag(name = "Movie", description = "영화 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    /**
     * 영화관 등록 처리
     * - 요청값 검증(@Valid)
     * - 관리자 권한(ROLE_ADMIN) 필요
     * - 등록 성공 시 201(CREATED) 반환
     */
    @Operation(summary = "영화 등록", description = "관리자 권한으로 새로운 영화를 등록합니다.")
    @PostMapping
    public ResponseEntity<?> createMovie(@Valid @RequestBody MovieCreateRequest req) {
        MovieCreateResponse res = movieService.createMovie(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(res, "영화 등록이 완료되었습니다."));
    }

    /**
     * 영화 목록 조회 요청 처리
     * - 검색어 및 검색조건, 등급, 개봉년도, 상영여부 필터 적용
     * - 커서 기반 페이지네이션 지원 (lastId, size)
     * - 조회 성공 시 200(OK) 상태 코드 반환
     */
    @Operation(summary = "영화 목록 조회", description = "영화 등급, 상영 여부, 개봉 연도 등의 조건으로 영화를 검색합니다.")
    @GetMapping
    public ResponseEntity<?> getMovies(@RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) Rating rating,
                                       @RequestParam(required = false) Boolean nowShowing,
                                       @RequestParam(required = false) Integer releaseYear,
                                       @RequestParam(defaultValue = "title") String searchBy,
                                       @RequestParam(required = false) Long lastId,
                                       @RequestParam(defaultValue = "10") int size) {
        MovieCursorResponse res = movieService.getMoviesByCursor(keyword, rating, nowShowing, releaseYear, searchBy,
                lastId, size);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "영화 목록 조회 성공"));
    }

    /**
     * 영화 상세 조회 요청 처리
     * - PathVariable로 ID 전달
     * - 조회 성공 시 200(OK) 상태 코드 반환
     */
    @Operation(summary = "영화 상세 조회", description = "ID로 영화의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovie(@PathVariable Long id) {
        MovieResponse res = movieService.getMovieById(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "영화 상세 조회 성공"));
    }

    /**
     * 영화 정보 수정 요청 처리
     * - PathVariable로 ID 전달
     * - 일부 필드만 선택적으로 수정 가능
     * - 관리자 권한(ROLE_ADMIN) 필요
     * - 수정 성공 시 200(OK) 반환
     */
    @Operation(summary = "영화 정보 수정", description = "관리자 권한으로 영화 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieUpdateRequest req) {
        MovieResponse res = movieService.updateMovie(id, req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "영화 정보 수정에 성공했습니다."));
    }

    /**
     * 영화 삭제 요청 처리
     * - PathVariable로 ID 전달
     * - 관리자 권한(ROLE_ADMIN) 필요
     * - 삭제 성공 시 204(NO_CONTENT) 반환
     */
    @Operation(summary = "영화 삭제", description = "관리자 권한으로 영화를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

}
