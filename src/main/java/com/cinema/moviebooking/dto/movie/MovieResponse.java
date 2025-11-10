package com.cinema.moviebooking.dto.movie;

import com.cinema.moviebooking.entity.Movie;
import com.cinema.moviebooking.entity.Rating;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MovieResponse {

    private Long id;
    private String title;
    private String director;
    private String genre;
    private Rating rating;
    private Boolean nowShowing;
    private Integer runningTimeMinutes;
    private LocalDate releaseDate;

    public static MovieResponse from(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .director(movie.getDirector())
                .genre(movie.getGenre())
                .rating(movie.getRating())
                .nowShowing(movie.getNowShowing())
                .runningTimeMinutes(movie.getRunningTimeMinutes())
                .releaseDate(movie.getReleaseDate())
                .build();
    }
}
