package com.cinema.moviebooking.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {

    CONFIRMED("예매 완료"),
    CANCELLED("예매 취소");

    private final String label;
}
