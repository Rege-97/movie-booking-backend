package com.cinema.moviebooking.dto.cinema;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CinemaUpdateRequest {

    @Size(max = 100, message = "이름은 100자 이하로 입력해주세요.")
    private String name;

    @Size(max = 100, message = "지역명은 100자 이하로 입력해주세요.")
    private String region;

    @Size(max = 255, message = "주소는 255자 이하로 입력해주세요.")
    private String address;

    @Size(max = 20, message = "연락처는 20자 이하로 입력해주세요.")
    private String contact;
}
