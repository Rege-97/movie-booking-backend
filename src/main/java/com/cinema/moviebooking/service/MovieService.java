package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.movie.MovieCreateRequest;
import com.cinema.moviebooking.dto.movie.MovieCreateResponse;
import com.cinema.moviebooking.entity.Movie;
import com.cinema.moviebooking.repository.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
