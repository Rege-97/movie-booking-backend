package com.cinema.moviebooking.repository.screening;

import com.cinema.moviebooking.entity.Theater;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.cinema.moviebooking.entity.QScreening.screening;

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
}
