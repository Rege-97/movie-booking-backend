package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.Screening.ScreeningCreateRequest;
import com.cinema.moviebooking.dto.Screening.ScreeningCreateResponse;
import com.cinema.moviebooking.entity.Movie;
import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.ScreeningStatus;
import com.cinema.moviebooking.entity.Theater;
import com.cinema.moviebooking.exception.DuplicateResourceException;
import com.cinema.moviebooking.exception.NotFoundException;
import com.cinema.moviebooking.repository.movie.MovieRepository;
import com.cinema.moviebooking.repository.screening.ScreeningRepository;
import com.cinema.moviebooking.repository.theater.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    /**
     * 상영스케쥴 등록
     * - 상영 영화 존재 여부 검증
     * - 상영관 존재 여부 검증
     * - 해당 상영관에 상영 시간이 겹치는 스케쥴이 있는지 검증
     * - 상영 종료 시간은 영화의 러닝타임을 더하여 저장
     * - 총 좌석수와 남은 좌석수는 상영관의 좌석 정보를 저장
     */
    @Transactional
    public ScreeningCreateResponse createScreening(ScreeningCreateRequest req) {
        Movie movie = movieRepository.findById(req.getMovieId())
                .orElseThrow(() -> new NotFoundException("해당 영화를 찾을 수 없습니다."));

        Theater theater = theaterRepository.findById(req.getTheaterId())
                .orElseThrow(() -> new NotFoundException("해당 상영관을 찾을 수 없습니다."));

        LocalDateTime startTime = req.getStartTime();
        LocalDateTime endTime = req.getStartTime().plusMinutes(movie.getRunningTimeMinutes());

        if (screeningRepository.existsByTheaterAndTimeRange(theater, startTime, endTime)) {
            throw new DuplicateResourceException("해당 시간대에 이미 상영이 예정된 상영관입니다.");
        }

        Screening screening = Screening.builder()
                .startTime(startTime)
                .endTime(endTime)
                .totalSeats(theater.getSeatCount())
                .availableSeats(theater.getSeatCount())
                .status(ScreeningStatus.SCHEDULED)
                .movie(movie)
                .theater(theater)
                .build();

        screeningRepository.save(screening);

        return new ScreeningCreateResponse(screening.getId());
    }
}
