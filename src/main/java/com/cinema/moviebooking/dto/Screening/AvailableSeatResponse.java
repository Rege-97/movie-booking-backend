package com.cinema.moviebooking.dto.Screening;

import com.cinema.moviebooking.entity.Seat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AvailableSeatResponse {

    private Long id;
    private String seatName;

    public static AvailableSeatResponse from(Seat seat) {
        return AvailableSeatResponse.builder()
                .id(seat.getId())
                .seatName(seat.getSeatRow().toString() + seat.getSeatNumber())
                .build();
    }
}
