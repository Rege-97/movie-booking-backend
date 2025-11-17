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
@Table(name = "screening", indexes = {
        @Index(name = "idx_screening_theater_status_start", columnList = "theater_id, status, startTime"),
        @Index(name = "idx_screening_status", columnList = "status")
})
public class Screening extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime openTime;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @Enumerated(EnumType.STRING)
    private ScreeningStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    private Theater theater;

    @Builder
    public Screening(LocalDateTime openTime, LocalDateTime startTime, LocalDateTime endTime, Integer totalSeats,
                     Integer availableSeats,
                     ScreeningStatus status, Movie movie, Theater theater) {
        this.openTime = openTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.status = status;
        this.movie = movie;
        this.theater = theater;
    }

    public void updateStatus(ScreeningStatus newStatus) {
        this.status = newStatus;
    }

    public void updateAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
}
