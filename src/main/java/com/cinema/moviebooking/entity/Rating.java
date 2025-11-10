package com.cinema.moviebooking.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rating {
    ALL("전체 관람가"),
    AGE12("12세 이상 관람가"),
    AGE15("15세 이상 관람가"),
    ADULT("청소년 관람불가");

    private final String description;
}