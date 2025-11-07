package com.cinema.moviebooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public Reservation(Long id, ReservationStatus status, Member member, Screening screening) {
        this.id = id;
        this.status = status;
        this.member = member;
        this.screening = screening;
    }
}
