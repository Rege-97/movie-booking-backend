package com.cinema.moviebooking.repository.screening;

import com.cinema.moviebooking.entity.Cinema;
import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.ScreeningStatus;
import com.cinema.moviebooking.entity.Theater;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.cinema.moviebooking.entity.QScreening.screening;
import static com.cinema.moviebooking.entity.QTheater.theater;
import static com.cinema.moviebooking.entity.QCinema.cinema;

@Repository
@RequiredArgsConstructor
public class ScreeningRepositoryImpl implements ScreeningRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByTheaterAndTimeRange(Theater theater, LocalDateTime startTime, LocalDateTime endTime) {
        return queryFactory
                .selectFrom(screening)
                .where(
                        screening.theater.eq(theater),
                        screening.startTime.before(endTime),
                        screening.endTime.after(startTime)
                )
                .fetchFirst() != null;
    }

    @Override
    public List<Screening> findValidByCinemaAndDate(Long cinemaId, LocalDate screeningDate) {
        return queryFactory
                .selectFrom(screening)
                .join(screening.theater, theater)
                .join(theater.cinema, cinema)
                .where(
                        cinema.id.eq(cinemaId),
                        screeningDateEq(screeningDate),
                        screening.status.eq(ScreeningStatus.SCHEDULED),
                        screening.startTime.goe(LocalDateTime.now())
                )
                .fetch();
    }

    private BooleanExpression screeningDateEq(LocalDate screeningDate) {
        return screening.startTime.year().eq(screeningDate.getYear())
                .and(screening.startTime.month().eq(screeningDate.getMonthValue()))
                .and(screening.startTime.dayOfMonth().eq(screeningDate.getDayOfMonth()));
    }
}
