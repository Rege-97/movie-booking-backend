package com.cinema.moviebooking.repository;

import com.cinema.moviebooking.entity.Cinema;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.cinema.moviebooking.entity.QCinema.cinema;

@Repository
@RequiredArgsConstructor
public class CinemaRepositoryImpl implements CinemaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Cinema> findByCursor(String keyword, String region, Long lastId, int size) {
        return queryFactory
                .selectFrom(cinema)
                .where(
                        nameContains(keyword),
                        regionEq(region),
                        idLt(lastId)
                )
                .orderBy(cinema.id.desc())
                .limit(size + 1)
                .fetch();
    }

    private BooleanExpression nameContains(String keyword) {
        return StringUtils.hasText(keyword) ? cinema.name.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression regionEq(String region) {
        return StringUtils.hasText(region) ? cinema.region.eq(region) : null;
    }

    private BooleanExpression idLt(Long lastId) {
        return lastId != null ? cinema.id.lt(lastId) : null;
    }
}
