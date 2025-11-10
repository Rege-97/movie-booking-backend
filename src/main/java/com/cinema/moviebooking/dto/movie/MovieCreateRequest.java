package com.cinema.moviebooking.dto.movie;

import com.cinema.moviebooking.entity.Rating;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MovieCreateRequest {

    @NotBlank(message = "영화 제목은 필수입니다.")
    @Size(max = 255, message = "영화 제목은 255자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "감독 이름은 필수입니다.")
    @Size(max = 200, message = "감독 이름은 200자 이하로 입력해주세요.")
    private String director;

    @NotBlank(message = "장르는 필수입니다.")
    @Size(max = 100, message = "장르는 100자 이하로 입력해주세요.")
    private String genre;

    @NotNull(message = "관람 등급은 필수입니다.")
    private Rating rating;

    private boolean nowShowing;

    @NotNull(message = "상영 시간은 필수입니다.")
    @Min(value = 30, message = "상영 시간은 최소 30분 이상이어야 합니다.")
    private Integer runningTimeMinutes;

    @NotNull(message = "개봉일은 필수입니다.")
    @PastOrPresent(message = "개봉일은 오늘 이전 또는 오늘 날짜여야 합니다.")
    private LocalDate releaseDate;
}