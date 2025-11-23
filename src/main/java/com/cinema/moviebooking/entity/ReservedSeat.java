package com.cinema.moviebooking.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_reserved_seat_screening_seat",
                columnNames = {"screening_id", "seat_id"}
        )
})
public class ReservedSeat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    private Reservation reservation;

    @ManyToOne
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    private Screening screening;

    @Builder
    public ReservedSeat(Long id, Reservation reservation, Seat seat, Screening screening) {
        this.id = id;
        this.reservation = reservation;
        this.seat = seat;
        this.screening = screening;
    }
}
