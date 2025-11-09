package com.cinema.moviebooking.dto.cinema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CinemaUpdateRequest {

    private String name;

    @Size(max = 100, message = "지역명은 100자 이하로 입력해주세요.")
    private String region;

    private String address;

    @Size(max = 20, message = "연락처는 20자 이하로 입력해주세요.")
    private String contact;
}
