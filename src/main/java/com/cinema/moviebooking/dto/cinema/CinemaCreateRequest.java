package com.cinema.moviebooking.dto.cinema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CinemaCreateRequest {

    @NotBlank(message = "영화관 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "지역은 필수입니다.")
    @Size(max = 100, message = "지역명은 100자 이하로 입력해주세요.")
    private String region;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @NotBlank(message = "연락처는 필수입니다.")
    @Size(max = 20, message = "연락처는 20자 이하로 입력해주세요.")
    private String contact;
}
