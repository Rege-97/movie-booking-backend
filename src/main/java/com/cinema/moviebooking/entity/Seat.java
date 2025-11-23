package com.cinema.moviebooking.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private Character seatRow;

    @Column(nullable = false)
    private Integer seatNumber;

    @Setter
    @ManyToOne
    private Theater theater;

    @Builder
    public Seat(Character seatRow, Integer seatNumber, Theater theater) {
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.theater = theater;
    }
}
