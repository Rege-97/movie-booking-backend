package com.cinema.moviebooking.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScreeningStatus {
    SCHEDULED("상영 예정"),
    ONGOING("상영 중"),
    COMPLETED("상영 종료"),
    CANCELED("상영 취소");

    private final String description;
}
