package com.cinema.moviebooking.dto.reservation;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationSeatRow {
    private Long reservationId;
    private Character seatRow;
    private Integer seatNumber;

    @QueryProjection
    public ReservationSeatRow(Long reservationId, Character seatRow, Integer seatNumber) {
        this.reservationId = reservationId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
    }
}