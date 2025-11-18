package com.cinema.moviebooking.repository.screening;

import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.ScreeningStatus;
import com.cinema.moviebooking.entity.Theater;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.cinema.moviebooking.entity.QCinema.cinema;
import static com.cinema.moviebooking.entity.QMovie.movie;
import static com.cinema.moviebooking.entity.QScreening.screening;
import static com.cinema.moviebooking.entity.QTheater.theater;

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

        LocalDateTime startOfDay = screeningDate.atStartOfDay();
        LocalDateTime endOfDay = screeningDate.atTime(23, 59, 59);

        return queryFactory
                .selectFrom(screening)
                .join(screening.theater, theater).fetchJoin()
                .join(screening.movie, movie).fetchJoin()
                .where(
                        theater.cinema.id.eq(cinemaId),
                        screening.startTime.between(startOfDay, endOfDay),
                        screening.status.eq(ScreeningStatus.SCHEDULED),
                        screening.startTime.goe(LocalDateTime.now())
                )
                .fetch();
    }

    @Transactional
    @Override
    public Long bulkUpdateStatus(List<Long> ids, ScreeningStatus fromStatus, ScreeningStatus toStatus) {
        if (ids == null || ids.isEmpty()) {
            return 0L;
        }

        return queryFactory.update(screening)
                .set(screening.status, toStatus)
                .where(
                        screening.id.in(ids),
                        screening.status.eq(fromStatus)
                )
                .execute();
    }
}
