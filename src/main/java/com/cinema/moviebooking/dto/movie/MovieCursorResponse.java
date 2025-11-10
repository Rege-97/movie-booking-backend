package com.cinema.moviebooking.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MovieCursorResponse {
    private List<MovieResponse> movies;
    private Long nextCursor;
    private boolean hasNext;
}
