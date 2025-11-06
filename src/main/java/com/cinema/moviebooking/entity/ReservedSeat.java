package com.cinema.moviebooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservedSeat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Reservation reservation;

    @ManyToOne
    private Seat seat;

    @Builder
    public ReservedSeat(Long id, Reservation reservation, Seat seat) {
        this.id = id;
        this.reservation = reservation;
        this.seat = seat;
    }
}
