package com.cinema.moviebooking.dto.reservation;

import com.cinema.moviebooking.entity.ReservationStatus;
import com.cinema.moviebooking.entity.ScreeningStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class MyReservationResponse {

    private Long reservationId;
    private String movieTitle;
    private String cinemaName;
    private String theaterName;
    @Setter
    private List<String> seats;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus reservationStatus;
    private ScreeningStatus screeningStatus;
    private LocalDateTime createdAt;

    @QueryProjection
    public MyReservationResponse(Long reservationId,
                                 String movieTitle,
                                 String cinemaName,
                                 String theaterName,
                                 LocalDateTime startTime,
                                 LocalDateTime endTime,
                                 ReservationStatus reservationStatus,
                                 ScreeningStatus screeningStatus,
                                 LocalDateTime createdAt) {
        this.reservationId = reservationId;
        this.movieTitle = movieTitle;
        this.cinemaName = cinemaName;
        this.theaterName = theaterName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationStatus = reservationStatus;
        this.screeningStatus = screeningStatus;
        this.createdAt = createdAt;
    }
}

