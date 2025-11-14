package com.cinema.moviebooking.repository.reservation;

import com.cinema.moviebooking.dto.reservation.MyReservationResponse;
import com.cinema.moviebooking.dto.reservation.QMyReservationResponse;
import com.cinema.moviebooking.dto.reservation.QReservationSeatRow;
import com.cinema.moviebooking.dto.reservation.ReservationSeatRow;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cinema.moviebooking.entity.QCinema.cinema;
import static com.cinema.moviebooking.entity.QMember.member;
import static com.cinema.moviebooking.entity.QMovie.movie;
import static com.cinema.moviebooking.entity.QReservation.reservation;
import static com.cinema.moviebooking.entity.QReservedSeat.reservedSeat;
import static com.cinema.moviebooking.entity.QScreening.screening;
import static com.cinema.moviebooking.entity.QSeat.seat;
import static com.cinema.moviebooking.entity.QTheater.theater;


@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MyReservationResponse> findReservationCursorByMember(Long memberId, Long lastId, int size) {

        return queryFactory
                .select(new QMyReservationResponse(
                        reservation.id,
                        movie.title,
                        cinema.name,
                        theater.name,
                        screening.startTime,
                        screening.endTime,
                        reservation.status,
                        screening.status,
                        reservation.createdAt
                ))
                .from(reservation)
                .join(reservation.screening, screening)
                .join(screening.theater, theater)
                .join(theater.cinema, cinema)
                .join(reservation.member, member)
                .join(screening.movie, movie)
                .where(
                        member.id.eq(memberId),
                        idLt(lastId)
                )
                .orderBy(reservation.id.desc())
                .limit(size + 1)
                .fetch();
    }

    @Override
    public List<ReservationSeatRow> findReservedSeatByReservationIds(List<Long> reservationIds) {
        if (reservationIds == null || reservationIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .select(new QReservationSeatRow(
                        reservation.id,
                        seat.seatRow,
                        seat.seatNumber
                ))
                .from(reservation)
                .join(reservation.reservedSeats, reservedSeat)
                .join(reservedSeat.seat, seat)
                .where(
                        reservation.id.in(reservationIds)
                )
                .fetch();
    }

    private BooleanExpression idLt(Long lastId) {
        return lastId != null ? reservation.id.lt(lastId) : null;
    }

}
