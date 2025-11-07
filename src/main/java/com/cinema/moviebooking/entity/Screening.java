package com.cinema.moviebooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screening extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Theater theater;

    @Builder
    public Screening(Long id, LocalDateTime startTime, Integer price, Movie movie, Theater theater) {
        this.id = id;
        this.startTime = startTime;
        this.price = price;
        this.movie = movie;
        this.theater = theater;
    }

}
