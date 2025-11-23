package com.cinema.moviebooking.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyReservationCursorResponse {

    private final List<MyReservationResponse> reservations;
    private Long nextCursor;
    private boolean hasNext;
}
