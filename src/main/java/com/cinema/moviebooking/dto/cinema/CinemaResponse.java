package com.cinema.moviebooking.dto.cinema;

import com.cinema.moviebooking.entity.Cinema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CinemaResponse {

    private Long id;
    private String name;
    private String region;
    private String address;
    private String contact;

    public static CinemaResponse from(Cinema cinema) {
        return CinemaResponse.builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .region(cinema.getRegion())
                .address(cinema.getAddress())
                .contact(cinema.getContact())
                .build();
    }
}
