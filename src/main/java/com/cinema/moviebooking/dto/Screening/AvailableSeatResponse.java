package com.cinema.moviebooking.dto.Screening;

import com.cinema.moviebooking.entity.Seat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AvailableSeatResponse {

    private Long id;
    private Character seatRow;
    private Integer seatNumber;

    public static AvailableSeatResponse from(Seat seat) {
        return AvailableSeatResponse.builder()
                .id(seat.getId())
                .seatRow(seat.getSeatRow())
                .seatNumber(seat.getSeatNumber())
                .build();
    }
}
