package com.cinema.moviebooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private Integer seatCount;

    @Enumerated(EnumType.STRING)
    private ScreenType screenType;

    @Column(nullable = false)
    private Boolean isAvailable;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Seat> seats = new ArrayList<>();

    @Builder
    public Theater(Long id, String name, Cinema cinema, Integer seatCount, ScreenType screenType, Boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.cinema = cinema;
        this.seatCount = seatCount;
        this.screenType = screenType;
        this.isAvailable = isAvailable;
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setTheater(this);
    }

    public void updateInfo(String name, ScreenType screenType, Boolean isAvailable) {
        if (name != null && !name.isBlank()) this.name = name;
        if (screenType != null) this.screenType = screenType;
        if (screenType != null) this.isAvailable = isAvailable;
    }
}
