package com.cinema.moviebooking.dto.theater;

import com.cinema.moviebooking.entity.ScreenType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TheaterCreateRequest {

    @NotBlank(message = "상영관 이름은 필수입니다.")
    @Size(max = 100, message = "상영관 이름은 100자 이하로 입력해주세요.")
    private String name;

    @NotNull(message = "소속 영화관 ID는 필수입니다.")
    private Long cinemaId;

    @Min(value = 10, message = "좌석 수는 10석 이상이어야 합니다.")
    private int seatCount;

    @NotNull(message = "상영관 타입은 필수입니다.")
    private ScreenType screenType;

    private boolean isAvailable;
}