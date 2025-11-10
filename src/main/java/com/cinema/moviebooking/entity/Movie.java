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
    private Rating rating;

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

    public void updateInfo(String title, String director, String genre, Rating rating, Boolean nowShowing,
                           Integer runningTimeMinutes, LocalDate releaseDate) {
        if (title != null && !title.isBlank()) this.title = title;
        if (director != null && !director.isBlank()) this.director = director;
        if (genre != null && !genre.isBlank()) this.genre = genre;
        if (rating != null) this.rating = rating;
        if (nowShowing != null) this.nowShowing = nowShowing;
        if (runningTimeMinutes != null) this.runningTimeMinutes = runningTimeMinutes;
        if (releaseDate != null) this.releaseDate = releaseDate;
    }

}
