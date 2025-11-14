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
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Screening screening;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReservedSeat> reservedSeats = new ArrayList<>();

    @Builder
    public Reservation(Long id, ReservationStatus status, Member member, Screening screening) {
        this.id = id;
        this.status = status;
        this.member = member;
        this.screening = screening;
    }

    public void addReservedSeat(ReservedSeat reservedSeats) {
        this.reservedSeats.add(reservedSeats);
        reservedSeats.setReservation(this);
    }

    public void updateState(ReservationStatus newStatus) {
        this.status = newStatus;
    }
}
