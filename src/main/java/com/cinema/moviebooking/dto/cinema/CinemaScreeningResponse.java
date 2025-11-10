package com.cinema.moviebooking.dto.cinema;

import com.cinema.moviebooking.entity.Cinema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CinemaScreeningResponse {
    private Long cinemaId;
    private String cinemaName;
    private List<MovieScreeningResponse> movieScreenings;

    public static CinemaScreeningResponse from(Cinema cinema, List<MovieScreeningResponse> movieScreenings) {
        return CinemaScreeningResponse.builder()
                .cinemaId(cinema.getId())
                .cinemaName(cinema.getName())
                .movieScreenings(movieScreenings)
                .build();
    }

}
