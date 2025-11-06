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
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer runningTimeMinutes;

    private LocalDate releaseDate;

    @Builder
    public Movie(String title, Integer runningTimeMinutes, LocalDate releaseDate) {
        this.title = title;
        this.runningTimeMinutes = runningTimeMinutes;
        this.releaseDate = releaseDate;
    }

}
