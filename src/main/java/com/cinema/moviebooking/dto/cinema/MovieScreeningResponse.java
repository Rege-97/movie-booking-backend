package com.cinema.moviebooking.dto.cinema;

import com.cinema.moviebooking.entity.Movie;
import com.cinema.moviebooking.entity.Rating;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MovieScreeningResponse {

    private Long movieId;
    private String movieTitle;
    private Rating rating;
    private List<TheaterScreeningResponse> theaterScreenings;

    public static MovieScreeningResponse from(Movie movie, List<TheaterScreeningResponse> theaterScreenings) {
        return MovieScreeningResponse.builder()
                .movieId(movie.getId())
                .movieTitle(movie.getTitle())
                .rating(movie.getRating())
                .theaterScreenings(theaterScreenings)
                .build();
    }
}
