package com.cinema.moviebooking.dto.cinema;

import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.ScreeningStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningResponse {

    private Long screeningId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalSeats;
    @Setter
    private Integer availableSeats;
    private ScreeningStatus status;

    public static ScreeningResponse from(Screening screening) {
        return ScreeningResponse.builder()
                .screeningId(screening.getId())
                .startTime(screening.getStartTime())
                .endTime(screening.getEndTime())
                .totalSeats(screening.getTotalSeats())
                .availableSeats(screening.getAvailableSeats())
                .status(screening.getStatus())
                .build();
    }
}
