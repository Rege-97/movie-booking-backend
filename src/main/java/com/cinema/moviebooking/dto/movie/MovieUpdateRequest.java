package com.cinema.moviebooking.dto.movie;

import com.cinema.moviebooking.entity.Rating;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MovieUpdateRequest {

    @Size(max = 255, message = "영화 제목은 255자 이하로 입력해주세요.")
    private String title;

    @Size(max = 200, message = "감독 이름은 200자 이하로 입력해주세요.")
    private String director;

    @Size(max = 100, message = "장르는 100자 이하로 입력해주세요.")
    private String genre;

    private Rating rating;

    private Boolean nowShowing;

    @Min(value = 30, message = "상영 시간은 최소 30분 이상이어야 합니다.")
    private Integer runningTimeMinutes;

    @PastOrPresent(message = "개봉일은 오늘 이전 또는 오늘 날짜여야 합니다.")
    private LocalDate releaseDate;
}