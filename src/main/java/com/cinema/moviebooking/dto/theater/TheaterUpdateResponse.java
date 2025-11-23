package com.cinema.moviebooking.dto.theater;

import com.cinema.moviebooking.entity.ScreenType;
import com.cinema.moviebooking.entity.Theater;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TheaterUpdateResponse {

    private Long id;
    private String name;
    private int seatCount;
    private ScreenType screenType;
    private boolean isAvailable;

    public static TheaterUpdateResponse from(Theater theater) {
        return TheaterUpdateResponse.builder()
                .id(theater.getId())
                .name(theater.getName())
                .seatCount(theater.getSeatCount())
                .screenType(theater.getScreenType())
                .isAvailable(theater.getIsAvailable())
                .build();
    }
}
