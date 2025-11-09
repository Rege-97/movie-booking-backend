package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.theater.TheaterCreateRequest;
import com.cinema.moviebooking.dto.theater.TheaterCreateResponse;
import com.cinema.moviebooking.entity.Cinema;
import com.cinema.moviebooking.entity.Theater;
import com.cinema.moviebooking.exception.DuplicateResourceException;
import com.cinema.moviebooking.exception.NotFoundException;
import com.cinema.moviebooking.repository.cinema.CinemaRepository;
import com.cinema.moviebooking.repository.theater.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final CinemaRepository cinemaRepository;
    private final TheaterRepository theaterRepository;

    /**
     * 상영관 등록
     * - 영화관 존재 여부 검증
     * - 상영관 이름 중복 체크
     * - 상영관 정보 저장 후 ID 반환
     */
    @Transactional
    public TheaterCreateResponse createTheater(TheaterCreateRequest req) {
        Cinema cinema = cinemaRepository.findById(req.getCinemaId())
                .orElseThrow(() -> new NotFoundException("해당 영화관을 찾을 수 없습니다."));

        if (theaterRepository.existsByCinemaAndName(cinema, req.getName())) {
            throw new DuplicateResourceException("이미 존재하는 상영관 이름입니다.");
        }

        Theater theater = Theater.builder()
                .name(req.getName())
                .cinema(cinema)
                .seatCount(req.getSeatCount())
                .screenType(req.getScreenType())
                .isAvailable(req.isAvailable())
                .build();

        theaterRepository.save(theater);

        return new TheaterCreateResponse(theater.getId());
    }
}
