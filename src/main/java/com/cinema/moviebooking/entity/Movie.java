package com.cinema.moviebooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 200)
    private String director;

    @Column(nullable = false, length = 100)
    private String genre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rating rating; // 관람등급 (전체, 12세, 15세, 청불 등)

    @Column(nullable = false)
    private Boolean nowShowing;

    @Column(nullable = false)
    private Integer runningTimeMinutes;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Builder
    public Movie(String title, String director, String genre, Rating rating, Boolean nowShowing,
                 Integer runningTimeMinutes, LocalDate releaseDate) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.rating = rating;
        this.nowShowing = nowShowing;
        this.runningTimeMinutes = runningTimeMinutes;
        this.releaseDate = releaseDate;
    }

}
