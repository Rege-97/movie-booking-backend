package com.cinema.moviebooking.repository.seat;

import com.cinema.moviebooking.entity.Seat;

import java.util.List;

public interface SeatRepositoryCustom {
    List<Seat> findAvailableSeatsByScreening(Long screeningId);
}
