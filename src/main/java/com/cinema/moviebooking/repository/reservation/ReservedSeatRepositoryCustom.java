package com.cinema.moviebooking.repository.reservation;

import java.util.List;

public interface ReservedSeatRepositoryCustom{
    List<Long> findReservedSeatIdsByScreeningAndSeats(Long screeningId, List<Long> seatIds);
}
