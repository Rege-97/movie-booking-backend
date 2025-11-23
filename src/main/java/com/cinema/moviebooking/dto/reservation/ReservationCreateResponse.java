package com.cinema.moviebooking.dto.reservation;

import com.cinema.moviebooking.entity.ReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReservationCreateResponse {

    private Long reservationId;
    private String movieTitle;
    private String theaterName;
    private List<String> seatNames;
    private LocalDateTime screeningTime;
    private ReservationStatus status;

}
