package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.cinema.CinemaCreateRequest;
import com.cinema.moviebooking.dto.cinema.CinemaCreateResponse;
import com.cinema.moviebooking.entity.Cinema;
import com.cinema.moviebooking.exception.DuplicateResourceException;
import com.cinema.moviebooking.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 영화관 관련 비즈니스 로직 처리
 * (영화관 등록, 조회, 수정, 삭제 등)
 */
@Service
@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    /**
     * 영화관 등록
     * - 영화관 이름 중복 체크 후 저장
     */
    @Transactional
    public CinemaCreateResponse createCinema(CinemaCreateRequest req) {
        if (cinemaRepository.existsByName(req.getName())) {
            throw new DuplicateResourceException("이미 존재하는 영화관 이름입니다.");
        }

        Cinema cinema = Cinema.builder()
                .name(req.getName())
                .region(req.getRegion())
                .address(req.getAddress())
                .contact(req.getContact())
                .build();

        cinemaRepository.save(cinema);

        return new CinemaCreateResponse(cinema.getId());
    }
}
