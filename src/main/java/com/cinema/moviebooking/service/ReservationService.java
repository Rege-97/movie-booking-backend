package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.reservation.*;
import com.cinema.moviebooking.entity.*;
import com.cinema.moviebooking.exception.InvalidStateException;
import com.cinema.moviebooking.exception.NotFoundException;
import com.cinema.moviebooking.exception.UnauthorizedReservationException;
import com.cinema.moviebooking.repository.reservation.ReservationRepository;
import com.cinema.moviebooking.repository.reservation.ReservedSeatRepository;
import com.cinema.moviebooking.repository.screening.ScreeningRepository;
import com.cinema.moviebooking.repository.seat.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 예매 관련 비즈니스 로직 처리
 * (예매 등록, 취소 등)
 */
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final ReservedSeatRepository reservedSeatRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String SEAT_COUNT_KEY = "screening:seats";

    /**
     * 예매 생성
     * - 상영 스케줄 존재 여부 및 예매 가능 상태 검증
     * - 요청된 좌석 ID 리스트의 유효성 검증 (존재하지 않는 좌석 포함 여부)
     * - 이미 예매된 좌석이 포함되어 있는지 검증
     * - Reservation 및 ReservedSeat 엔티티 생성 및 저장
     * - 상영 스케줄의 남은 좌석 수 갱신
     */
    @Transactional
    public ReservationCreateResponse createReservation(Member Member, ReservationCreateRequest req) {

        // 상영 스케줄 검증
        Screening screening = screeningRepository.findById(req.getScreeningId())
                .orElseThrow(() -> new NotFoundException("해당 영화 스케줄을 찾을 수 없습니다."));

        if (screening.getStatus() != ScreeningStatus.SCHEDULED) {
            throw new InvalidStateException("해당 스케줄은 예매 가능 상태가 아닙니다.");
        }

        // 좌석 존재 여부 검증
        List<Seat> seats = seatRepository.findAllById(req.getSeatIdList());
        if (seats.size() != req.getSeatIdList().size()) {
            throw new NotFoundException("요청된 좌석 중 일부를 찾을 수 없습니다.");
        }

        // 이미 예매된 좌석 검증
        List<Long> reservedSeatIds = reservedSeatRepository.findReservedSeatIdsByScreeningAndSeats(screening.getId(),
                req.getSeatIdList());
        if (!reservedSeatIds.isEmpty()) {
            throw new InvalidStateException("이미 예매된 좌석이 포함되어 있습니다: " + reservedSeatIds);
        }

        // Redis 좌석 차감
        long reservedCount = (long) req.getSeatIdList().size();
        Long newAvailableCount = redisTemplate.opsForHash().increment(
                SEAT_COUNT_KEY,
                req.getScreeningId().toString(),
                -reservedCount
        );

        // Redis 상에서 좌석이 부족하면 즉시 원복 후 실패 처리
        if (newAvailableCount < 0) {
            redisTemplate.opsForHash().increment(SEAT_COUNT_KEY, req.getScreeningId().toString(), reservedCount);
            throw new InvalidStateException("좌석이 부족합니다.");
        }
        try {
            // 예매 생성
            Reservation reservation = Reservation.builder()
                    .status(ReservationStatus.CONFIRMED)
                    .member(Member)
                    .screening(screening)
                    .build();

            List<String> seatNames = new ArrayList<>();

            for (Seat seat : seats) {
                seatNames.add(seat.getSeatRow().toString() + seat.getSeatNumber());

                ReservedSeat reservedSeat = ReservedSeat.builder()
                        .reservation(reservation)
                        .seat(seat)
                        .screening(screening)
                        .build();

                reservation.addReservedSeat(reservedSeat);
            }

            reservationRepository.save(reservation);

            // 남은 좌석 수 갱신
            screening.updateAvailableSeats(newAvailableCount.intValue());

            reservationRepository.flush();

            return ReservationCreateResponse.builder()
                    .reservationId(reservation.getId())
                    .theaterName(screening.getTheater().getName())
                    .movieTitle(screening.getMovie().getTitle())
                    .status(reservation.getStatus())
                    .seatNames(seatNames)
                    .screeningTime(screening.getStartTime())
                    .build();

        } catch (Exception e) {
            redisTemplate.opsForHash().increment(
                    SEAT_COUNT_KEY,
                    req.getScreeningId().toString(),
                    reservedCount
            );
            // 상위(Controller)로 예외를 다시 던져서 트랜잭션 롤백 및 에러 응답 유도
            throw e;
        }
    }

    /**
     * 내 예매 목록 조회 (커서 기반)
     * - 마지막 ID(lastId)를 기준으로 다음 데이터 조회
     * - 조회 결과 크기(size)에 따라 다음 페이지 존재 여부(hasNext) 계산
     * - 응답 데이터에 nextCursor 포함 (다음 요청 시 기준점)
     */
    @Transactional(readOnly = true)
    public MyReservationCursorResponse getMyReservationCursor(Member member, Long lastId, Integer size) {

        // 예매 목록 조회
        List<MyReservationResponse> reservations =
                reservationRepository.findReservationCursorByMember(member.getId(), lastId, size + 1);

        if (reservations.isEmpty()) {
            return new MyReservationCursorResponse(List.of(), null, false);
        }

        // 예매된 좌석들 조회 및 그룹핑
        List<Long> reservationIds = reservations.stream()
                .map(MyReservationResponse::getReservationId)
                .toList();

        List<ReservationSeatRow> seatRows =
                reservationRepository.findReservedSeatByReservationIds(reservationIds);

        Map<Long, List<String>> seatMap = new HashMap<>();
        for (ReservationSeatRow row : seatRows) {
            seatMap.computeIfAbsent(row.getReservationId(), k -> new ArrayList<>())
                    .add(row.getSeatRow() + row.getSeatNumber().toString());
        }

        for (MyReservationResponse res : reservations) {
            res.setSeats(seatMap.getOrDefault(res.getReservationId(), List.of()));
        }

        // 반환 데이터 조립
        boolean hasNext = reservations.size() > size;

        if (hasNext) {
            reservations = reservations.subList(0, size);
        }

        Long nextCursor = (hasNext && !reservations.isEmpty())
                ? reservations.get(reservations.size() - 1).getReservationId()
                : null;

        return new MyReservationCursorResponse(reservations, nextCursor, hasNext);
    }

    /**
     * 예매 취소
     * - 예매 존재 여부 검증
     * - 로그인 사용자 검증
     * - 현재 상태 검증
     * - 취소 가능 시간 검증
     * - 취소 가능 상태일 경우 상태를 CANCELED로 변경
     */
    @Transactional
    public void cancelReservation(Member member, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("해당 예매를 찾을 수 없습니다."));

        if (!reservation.getMember().getId().equals(member.getId())) {
            throw new UnauthorizedReservationException("로그인한 사용자의 예매가 아닙니다.");
        }

        Screening screening = reservation.getScreening();

        if (reservation.getStatus().equals(ReservationStatus.CANCELLED)) {
            throw new InvalidStateException("해당 예매는 이미 취소됐습니다.");
        }

        LocalDateTime cancelDeadline = screening.getStartTime().minusMinutes(15);
        if (LocalDateTime.now().isAfter(cancelDeadline)) {
            throw new InvalidStateException("상영 시작 15분 전까지만 예매를 취소할 수 있습니다.");
        }

        long canceledSeatCount = reservation.getReservedSeats().size();
        Long newAvailableCount = redisTemplate.opsForHash().increment(
                SEAT_COUNT_KEY,
                screening.getId().toString(),
                (long) canceledSeatCount
        );

        screening.updateAvailableSeats(newAvailableCount.intValue());

        reservation.updateState(ReservationStatus.CANCELLED);
    }
}
