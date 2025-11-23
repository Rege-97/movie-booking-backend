package com.cinema.moviebooking.dto.theater;

import com.cinema.moviebooking.entity.ScreenType;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "좌석 행 개수는 필수 입력 항목입니다.")
    @Min(value = 3, message = "좌석 행은 최소 3 이상이어야 합니다.")
    @Max(value = 20, message = "좌석 행은 최대 20 이하로 입력해야 합니다.")
    private Integer seatRowCount;

    @NotNull(message = "좌석 열 개수는 필수 입력 항목입니다.")
    @Min(value = 5, message = "좌석 열은 최소 5 이상이어야 합니다.")
    @Max(value = 30, message = "좌석 열은 최대 30 이하로 입력해야 합니다.")
    private Integer seatColumnCount;

    @NotNull(message = "상영관 타입은 필수입니다.")
    private ScreenType screenType;

    private Boolean available;
}