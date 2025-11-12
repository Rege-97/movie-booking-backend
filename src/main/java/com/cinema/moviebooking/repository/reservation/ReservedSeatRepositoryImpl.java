package com.cinema.moviebooking.repository.reservation;

import com.cinema.moviebooking.entity.ReservationStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cinema.moviebooking.entity.QReservation.reservation;
import static com.cinema.moviebooking.entity.QReservedSeat.reservedSeat;
import static com.cinema.moviebooking.entity.QSeat.seat;

@Repository
@RequiredArgsConstructor
public class ReservedSeatRepositoryImpl implements ReservedSeatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findReservedSeatIdsByScreeningAndSeats(Long screeningId, List<Long> seatIds) {
        return queryFactory
                .select(seat.id)
                .from(reservedSeat)
                .join(reservation, reservedSeat.reservation)
                .join(seat, reservedSeat.seat)
                .where(
                        reservation.screening.id.eq(screeningId),
                        reservation.status.eq(ReservationStatus.CONFIRMED),
                        seat.id.in(seatIds)
                )
                .fetch();
    }
}
