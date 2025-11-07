package com.cinema.moviebooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String seatRow;

    @Column(nullable = false)
    private Integer seatNumber;

    @ManyToOne
    private Theater theater;

    @Builder
    public Seat(String seatRow, Integer seatNumber, Theater theater) {
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.theater = theater;
    }
}
