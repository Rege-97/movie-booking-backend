package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.movie.*;
import com.cinema.moviebooking.entity.Movie;
import com.cinema.moviebooking.entity.Rating;
import com.cinema.moviebooking.exception.NotFoundException;
import com.cinema.moviebooking.repository.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 영화 관련 비즈니스 로직 처리
 * (영화 등록, 조회, 수정, 삭제 등)
 */
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    /**
     * 영화 등록
     * - 영화 정보 요청 받은 후 저장
     */
    @Transactional
    public MovieCreateResponse createMovie(MovieCreateRequest req) {
        Movie movie = Movie.builder()
                .title(req.getTitle())
                .director(req.getDirector())
                .genre(req.getGenre())
                .rating(req.getRating())
                .nowShowing(req.isNowShowing())
                .runningTimeMinutes(req.getRunningTimeMinutes())
                .releaseDate(req.getReleaseDate())
                .build();

        movieRepository.save(movie);

        return new MovieCreateResponse(movie.getId());
    }

    /**
     * 영화 목록 조회 (커서 기반)
     * - 검색어 및 검색조건, 등급, 개봉년도, 상영여부 필터 적용
     * - 마지막 ID(lastId)를 기준으로 다음 데이터 조회
     * - 조회 결과 크기(size)에 따라 다음 페이지 존재 여부(hasNext) 계산
     * - 응답 데이터에 nextCursor 포함 (다음 요청 시 기준점)
     */
    @Transactional(readOnly = true)
    public MovieCursorResponse getMoviesByCursor(String keyword, Rating rating, Boolean nowShowing,
                                                 Integer releaseYear, String searchBy, Long lastId,
                                                 int size) {
        List<Movie> movies = movieRepository.findByCursor(keyword, rating, nowShowing, releaseYear, searchBy, lastId,
                size + 1);
        boolean hasNext = movies.size() > size;

        List<MovieResponse> responses = movies.stream()
                .limit(size)
                .map(MovieResponse::from)
                .toList();

        Long nextCursor = (hasNext && !responses.isEmpty())
                ? responses.get(responses.size() - 1).getId() : null;

        return new MovieCursorResponse(responses, nextCursor, hasNext);
    }

    /**
     * 영화 상세 조회
     * - 영화 존재 검증 후 반환
     */
    @Transactional(readOnly = true)
    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 영화를 찾을 수 없습니다."));

        return MovieResponse.from(movie);
    }

    /**
     * 영화 수정
     * - 영화 존재 검증
     * - 영화 정보 수정 후 반환
     */
    @Transactional
    public MovieResponse updateMovie(Long id, MovieUpdateRequest req) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 영화를 찾을 수 없습니다."));

        movie.updateInfo(req.getTitle(), req.getDirector(), req.getGenre(), req.getRating(), req.getNowShowing(),
                req.getRunningTimeMinutes(), req.getReleaseDate());

        return MovieResponse.from(movie);
    }

    /**
     * 영화 삭제
     * - 영화 존재 여부 검증
     * - 삭제 성공 시 트랜잭션 커밋
     */
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 영화를 찾을 수 없습니다."));

        movieRepository.delete(movie);
    }
}
