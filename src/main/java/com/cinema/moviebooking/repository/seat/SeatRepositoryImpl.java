package com.cinema.moviebooking.repository.seat;

import com.cinema.moviebooking.entity.QReservation;
import com.cinema.moviebooking.entity.QReservedSeat;
import com.cinema.moviebooking.entity.ReservationStatus;
import com.cinema.moviebooking.entity.Seat;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cinema.moviebooking.entity.QScreening.screening;
import static com.cinema.moviebooking.entity.QSeat.seat;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 특정 상영(screeningId)에 대해
     * 이미 예매(confirmed)된 좌석을 제외한 '예약 가능한 좌석' 목록을 조회
     */
    @Override
    public List<Seat> findAvailableSeatsByScreening(Long screeningId) {

        QReservedSeat subReservedSeat = new QReservedSeat("subReservedSeat");
        QReservation subReservation = new QReservation("subReservation");

        // 예매 완료(CONFIRMED)된 좌석 ID 조회 서브쿼리
        JPQLQuery<Long> reservedSeatIdsSubQuery = JPAExpressions
                .select(subReservedSeat.seat.id)
                .from(subReservedSeat)
                .join(subReservedSeat.reservation, subReservation)
                .where(
                        subReservation.screening.id.eq(screeningId),
                        subReservation.status.eq(ReservationStatus.CONFIRMED)
                );

        // 상영관의 좌석 중 예약되지 않은 좌석 조회
        return queryFactory
                .selectFrom(seat)
                .join(screening).on(seat.theater.eq(screening.theater))
                .where(
                        screening.id.eq(screeningId),
                        seat.id.notIn(reservedSeatIdsSubQuery)
                )
                .fetch();
    }
}
