package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.movie.MovieCreateRequest;
import com.cinema.moviebooking.dto.movie.MovieCreateResponse;
import com.cinema.moviebooking.dto.movie.MovieCursorResponse;
import com.cinema.moviebooking.dto.movie.MovieResponse;
import com.cinema.moviebooking.entity.Rating;
import com.cinema.moviebooking.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 영화 관련 요청을 처리하는 컨트롤러
 * (영화 등록, 수정, 삭제 등)
 */
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovie(@PathVariable Long id) {
        MovieResponse res = movieService.getMovieById(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "영화 상세 조회 성공"));
    }
}
