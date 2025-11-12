package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.reservation.ReservationCreateRequest;
import com.cinema.moviebooking.dto.reservation.ReservationCreateResponse;
import com.cinema.moviebooking.entity.*;
import com.cinema.moviebooking.exception.InvalidStateException;
import com.cinema.moviebooking.exception.NotFoundException;
import com.cinema.moviebooking.repository.reservation.ReservationRepository;
import com.cinema.moviebooking.repository.reservation.ReservedSeatRepository;
import com.cinema.moviebooking.repository.screening.ScreeningRepository;
import com.cinema.moviebooking.repository.seat.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
                    .build();

            reservation.addReservedSeat(reservedSeat);
        }

        reservationRepository.save(reservation);

        // 남은 좌석 수 갱신
        screening.updateAvailableSeats(screening.getAvailableSeats() - seats.size());

        return ReservationCreateResponse.builder()
                .reservationId(reservation.getId())
                .theaterName(screening.getTheater().getName())
                .movieTitle(screening.getMovie().getTitle())
                .status(reservation.getStatus())
                .seatNames(seatNames)
                .screeningTime(screening.getStartTime())
                .build();
    }
}
