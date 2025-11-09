package com.cinema.moviebooking.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScreenType {
    _2D("일반 2D 상영"),
    _3D("3D 상영"),
    IMAX("IMAX 상영"),
    _4DX("4DX 상영"),
    DOLBY("Dolby Cinema 상영");

    private final String description;
}