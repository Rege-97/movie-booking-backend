package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.Screening.AvailableSeatResponse;
import com.cinema.moviebooking.dto.Screening.ScreeningCreateRequest;
import com.cinema.moviebooking.dto.Screening.ScreeningCreateResponse;
import com.cinema.moviebooking.entity.*;
import com.cinema.moviebooking.exception.DuplicateResourceException;
import com.cinema.moviebooking.exception.InvalidRequestException;
import com.cinema.moviebooking.exception.InvalidStateException;
import com.cinema.moviebooking.exception.NotFoundException;
import com.cinema.moviebooking.repository.movie.MovieRepository;
import com.cinema.moviebooking.repository.screening.ScreeningRepository;
import com.cinema.moviebooking.repository.seat.SeatRepository;
import com.cinema.moviebooking.repository.theater.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * 상영스케줄 관련 비즈니스 로직 처리
 * (상영스케줄 등록, 수정, 삭제 등)
 */
@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String SEAT_COUNT_KEY = "screening:seats";
    private static final String SCREENING_TASK_KEY = "screening:tasks";

    /**
     * 상영스케쥴 등록
     * - 상영 영화 존재 여부 검증
     * - 상영관 존재 여부 검증
     * - 해당 상영관에 상영 시간이 겹치는 스케쥴이 있는지 검증
     * - 상영 종료 시간은 영화의 러닝타임을 더하여 저장
     * - 총 좌석수와 남은 좌석수는 상영관의 좌석 정보를 저장
     */
    @Transactional
    @CacheEvict(value = "cinemaScreening", allEntries = true)
    public ScreeningCreateResponse createScreening(ScreeningCreateRequest req) {
        Movie movie = movieRepository.findById(req.getMovieId())
                .orElseThrow(() -> new NotFoundException("해당 영화를 찾을 수 없습니다."));

        Theater theater = theaterRepository.findById(req.getTheaterId())
                .orElseThrow(() -> new NotFoundException("해당 상영관을 찾을 수 없습니다."));

        if (req.getOpenTime().isAfter(req.getStartTime())) {
            throw new InvalidRequestException("예매 오픈 시간은 상영 시작 시간보다 이전이어야 합니다.");
        }

        LocalDateTime startTime = req.getStartTime();
        LocalDateTime endTime = req.getStartTime().plusMinutes(movie.getRunningTimeMinutes());

        if (screeningRepository.existsByTheaterAndTimeRange(theater, startTime, endTime)) {
            throw new DuplicateResourceException("해당 시간대에 이미 상영이 예정된 상영관입니다.");
        }

        Screening screening = Screening.builder()
                .openTime(req.getOpenTime())
                .startTime(startTime)
                .endTime(endTime)
                .totalSeats(theater.getSeatCount())
                .availableSeats(theater.getSeatCount())
                .status(ScreeningStatus.PENDING)
                .movie(movie)
                .theater(theater)
                .build();

        screeningRepository.save(screening);

        redisTemplate.opsForHash().put(
                SEAT_COUNT_KEY,
                screening.getId().toString(),
                theater.getSeatCount().toString()
        );

        String screeningId = screening.getId().toString();
        redisTemplate.opsForZSet().add(
                SCREENING_TASK_KEY,
                screeningId + ":OPEN",
                req.getOpenTime().toEpochSecond(ZoneOffset.UTC)
        );
        redisTemplate.opsForZSet().add(
                SCREENING_TASK_KEY,
                screeningId + ":START",
                startTime.toEpochSecond(ZoneOffset.UTC)
        );
        redisTemplate.opsForZSet().add(
                SCREENING_TASK_KEY,
                screeningId + ":END",
                endTime.toEpochSecond(ZoneOffset.UTC)
        );


        return new ScreeningCreateResponse(screening.getId());
    }

    /**
     * 상영 스케줄 취소
     * - 완료, 취소, 상영 중 상태는 취소 불가
     * - 취소 가능 상태일 경우 상태를 CANCELED로 변경
     */
    @Transactional
    @CacheEvict(value = "cinemaScreening", allEntries = true)
    public void cancelScreening(Long id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 상영 스케줄을 찾을 수 없습니다."));

        if (screening.getStatus() == ScreeningStatus.COMPLETED
                || screening.getStatus() == ScreeningStatus.CANCELED
                || screening.getStatus() == ScreeningStatus.ONGOING) {
            throw new InvalidStateException("상영 중이거나 이미 완료·취소된 상영은 취소할 수 없습니다.");
        }
        redisTemplate.opsForHash().delete(SEAT_COUNT_KEY, id.toString());
        String screeningId = id.toString();
        redisTemplate.opsForZSet().remove(
                SCREENING_TASK_KEY,
                screeningId + ":OPEN",
                screeningId + ":START",
                screeningId + ":END"
        );

        screening.updateStatus(ScreeningStatus.CANCELED);
    }

    /**
     * 예약 가능 좌석 조회
     * - 상영 스케줄 존재 여부 검증
     * - 해당 상영 스케줄에서 이미 예약된 좌석을 제외한 잔여 좌석 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AvailableSeatResponse> getAvailableSeat(Long id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 상영 스케줄을 찾을 수 없습니다."));

        List<Seat> seats = seatRepository.findAvailableSeatsByScreening(id);

        return seats.stream()
                .map(AvailableSeatResponse::from)
                .toList();
    }
}
