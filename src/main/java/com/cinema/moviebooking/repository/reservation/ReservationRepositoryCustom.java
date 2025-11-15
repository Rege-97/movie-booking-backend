package com.cinema.moviebooking.repository.reservation;

import com.cinema.moviebooking.dto.reservation.MyReservationResponse;
import com.cinema.moviebooking.dto.reservation.ReservationSeatRow;

import java.util.List;

public interface ReservationRepositoryCustom {
    List<MyReservationResponse> findReservationCursorByMember(Long memberId, Long lastId, int size);

    List<ReservationSeatRow> findReservedSeatByReservationIds(List<Long> reservationIds) ;
}
