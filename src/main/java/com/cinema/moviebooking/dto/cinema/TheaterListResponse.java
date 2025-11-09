package com.cinema.moviebooking.dto.cinema;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TheaterListResponse {
    private CinemaResponse cinemaInfo;
    private List<TheaterResponse> theaters;
}
