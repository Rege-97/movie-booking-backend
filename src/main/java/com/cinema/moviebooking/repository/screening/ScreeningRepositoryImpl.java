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

    @Override
    public List<Screening> findScreeningsForStatusUpdate(ScreeningStatus status, LocalDateTime now) {
        return queryFactory
                .selectFrom(screening)
                .where(
                        screening.status.eq(status),
                        buildTimeCondition(status, now)
                )
                .fetch();
    }

    @Transactional
    @Override
    public void updateToOngoingIfStarted(LocalDateTime now) {
        queryFactory.update(screening)
                .where(
                        screening.status.eq(ScreeningStatus.SCHEDULED),
                        buildTimeCondition(ScreeningStatus.SCHEDULED, now)
                )
                .set(screening.status, ScreeningStatus.ONGOING)
                .execute();
    }

    @Transactional
    @Override
    public void updateToCompletedIfEnded(LocalDateTime now) {
        queryFactory.update(screening)
                .where(
                        screening.status.eq(ScreeningStatus.ONGOING),
                        buildTimeCondition(ScreeningStatus.ONGOING, now)
                )
                .set(screening.status, ScreeningStatus.COMPLETED)
                .execute();
    }

    private BooleanExpression screeningDateEq(LocalDate screeningDate) {
        return screening.startTime.year().eq(screeningDate.getYear())
                .and(screening.startTime.month().eq(screeningDate.getMonthValue()))
                .and(screening.startTime.dayOfMonth().eq(screeningDate.getDayOfMonth()));
    }

    /**
     * 상영 상태에 따른 시간 조건 생성
     * - 예정(SCHEDULED): 시작 15분 전이면 true
     * - 상영중(ONGOING): 종료 시간이 현재 시각보다 같거나 이르면 true
     */
    private BooleanExpression buildTimeCondition(ScreeningStatus status, LocalDateTime now) {
        if (status == ScreeningStatus.SCHEDULED) {
            return screening.startTime.loe(now.plusMinutes(15));
        } else if (status == ScreeningStatus.ONGOING) {
            return screening.endTime.loe(now);
        } else {
            return null;
        }
    }
}
