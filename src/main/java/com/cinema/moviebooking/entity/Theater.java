package com.cinema.moviebooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @Column(nullable = false)
    private int seatCount;

    @Enumerated(EnumType.STRING)
    private ScreenType screenType;

    @Column(nullable = false)
    private boolean isAvailable;

    @Builder
    public Theater(Long id, String name, Cinema cinema, int seatCount, ScreenType screenType, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.cinema = cinema;
        this.seatCount = seatCount;
        this.screenType = screenType;
        this.isAvailable = isAvailable;
    }
}
