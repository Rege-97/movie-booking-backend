package com.cinema.moviebooking.dto.Screening;

import com.cinema.moviebooking.entity.ScreeningStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScreeningCreateRequest {

    @NotNull(message = "상영 시작 시간은 필수입니다.")
    @FutureOrPresent(message = "상영 시작 시간은 오늘 이후 또는 오늘이어야 합니다.")
    private LocalDateTime startTime;

    @NotNull(message = "영화 ID는 필수입니다.")
    private Long movieId;

    @NotNull(message = "상영관 ID는 필수입니다.")
    private Long theaterId;
}
