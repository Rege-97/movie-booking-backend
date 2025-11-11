package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.theater.TheaterCreateRequest;
import com.cinema.moviebooking.dto.theater.TheaterCreateResponse;
import com.cinema.moviebooking.dto.theater.TheaterUpdateRequest;
import com.cinema.moviebooking.dto.theater.TheaterUpdateResponse;
import com.cinema.moviebooking.entity.Cinema;
import com.cinema.moviebooking.entity.Seat;
import com.cinema.moviebooking.entity.Theater;
import com.cinema.moviebooking.exception.DuplicateResourceException;
import com.cinema.moviebooking.exception.NotFoundException;
import com.cinema.moviebooking.repository.cinema.CinemaRepository;
import com.cinema.moviebooking.repository.seat.SeatRepository;
import com.cinema.moviebooking.repository.theater.TheaterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상영관 관련 비즈니스 로직 처리
 * (상영관 등록, 수정, 삭제 등)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TheaterService {

    private final CinemaRepository cinemaRepository;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    /**
     * 상영관 등록
     * - 영화관 존재 여부 검증
     * - 상영관 이름 중복 체크
     * - 상영관 정보 저장 후 ID 반환
     * - 입력받은 행(Row) × 열(Column) 수에 따라 좌석 자동 생성
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
                .seatCount(req.getSeatColumnCount() * req.getSeatRowCount())
                .screenType(req.getScreenType())
                .isAvailable(req.getAvailable())
                .build();

        theaterRepository.save(theater);

        char seatRow = 'A';

        for (int i = 1; i <= req.getSeatRowCount(); i++) {
            for (int j = 1; j <= req.getSeatColumnCount(); j++) {
                Seat seat = Seat.builder()
                        .seatRow(seatRow)
                        .seatNumber(j)
                        .theater(theater)
                        .build();

                theater.addSeat(seat);
            }
            seatRow++;
        }

        log.info("상영관 [{}] 등록 완료 - 총 {}석 ({}행 × {}열)",
                theater.getName(),
                theater.getSeatCount(),
                req.getSeatRowCount(),
                req.getSeatColumnCount());

        return new TheaterCreateResponse(theater.getId());
    }

    /**
     * 상영관 수정
     * - 상영관 존재 검증
     * - 상영관 정보 수정 후 반환
     */
    @Transactional
    public TheaterUpdateResponse updateTheater(Long id, TheaterUpdateRequest req) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 상영관을 찾을 수 없습니다."));

        theater.updateInfo(req.getName(), req.getScreenType(), req.getAvailable());
        return TheaterUpdateResponse.from(theater);
    }

    /**
     * 상영관 삭제
     * - 상영관 존재 여부 검증
     * - 삭제 성공 시 트랜잭션 커밋
     */
    @Transactional
    public void deleteTheater(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 상영관을 찾을 수 없습니다."));

        theaterRepository.delete(theater);
    }
}
