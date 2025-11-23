package com.cinema.moviebooking.dto.cinema;

import com.cinema.moviebooking.entity.ScreenType;
import com.cinema.moviebooking.entity.Theater;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TheaterScreeningResponse {
    private Long theaterId;
    private String theaterName;
    private ScreenType screenType;
    private List<ScreeningResponse> screenings;

    public static TheaterScreeningResponse from(Theater theater, List<ScreeningResponse> screenings) {
        return TheaterScreeningResponse.builder()
                .theaterId(theater.getId())
                .theaterName(theater.getName())
                .screenType(theater.getScreenType())
                .screenings(screenings)
                .build();
    }
}
