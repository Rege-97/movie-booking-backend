package com.cinema.moviebooking.dto.cinema;

import com.cinema.moviebooking.entity.ScreenType;
import com.cinema.moviebooking.entity.Theater;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TheaterResponse {

    private Long id;
    private String name;
    private int seatCount;
    private ScreenType screenType;
    private boolean isAvailable;

    public static TheaterResponse from(Theater theater) {
        return TheaterResponse.builder()
                .id(theater.getId())
                .name(theater.getName())
                .seatCount(theater.getSeatCount())
                .screenType(theater.getScreenType())
                .isAvailable(theater.getIsAvailable())
                .build();
    }
}
