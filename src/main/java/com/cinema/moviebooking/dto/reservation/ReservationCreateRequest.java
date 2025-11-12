package com.cinema.moviebooking.dto.reservation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReservationCreateRequest {

    @NotNull(message = "상영 스케줄 ID는 필수 입력 값입니다.")
    private Long screeningId;

    @NotEmpty(message = "최소 1개 이상의 좌석을 선택해야 합니다.")
    private List<@NotNull(message = "좌석 ID는 null일 수 없습니다.") Long> seatIdList;
}
