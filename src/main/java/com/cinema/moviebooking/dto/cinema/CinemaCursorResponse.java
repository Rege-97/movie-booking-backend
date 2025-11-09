package com.cinema.moviebooking.dto.cinema;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CinemaCursorResponse {
    private List<CinemaListResponse> cinemas;
    private Long nextCursor;
    private boolean hasNext;

}
