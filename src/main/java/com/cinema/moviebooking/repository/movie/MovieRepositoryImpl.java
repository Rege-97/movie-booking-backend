package com.cinema.moviebooking.repository.movie;

import com.cinema.moviebooking.entity.Movie;
import com.cinema.moviebooking.entity.Rating;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.cinema.moviebooking.entity.QMovie.movie;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Movie> findByCursor(String keyword, Rating rating, Boolean nowShowing,
                                    Integer releaseYear, String searchBy, Long lastId, int size) {
        return queryFactory
                .selectFrom(movie)
                .where(
                        keywordContains(keyword, searchBy),
                        ratingEq(rating),
                        releaseYearEq(releaseYear),
                        nowShowingEq(nowShowing),
                        idLt(lastId),
                        movie.deletedAt.isNull()
                )
                .orderBy(movie.id.desc())
                .limit(size + 1)
                .fetch();
    }

    private BooleanExpression keywordContains(String keyword, String searchBy) {
        if (!StringUtils.hasText(keyword) || !StringUtils.hasText(searchBy)) {
            return null;
        }

        return switch (searchBy.toLowerCase()) {
            case "title" -> movie.title.containsIgnoreCase(keyword);
            case "genre" -> movie.genre.containsIgnoreCase(keyword);
            case "director" -> movie.director.containsIgnoreCase(keyword);
            default -> null;
        };
    }

    private BooleanExpression ratingEq(Rating rating) {
        return rating != null ? movie.rating.eq(rating) : null;
    }

    private BooleanExpression releaseYearEq(Integer releaseYear) {
        return releaseYear != null ? movie.releaseDate.year().eq(releaseYear) : null;
    }

    private BooleanExpression nowShowingEq(Boolean nowShowing) {
        return nowShowing != null ? movie.nowShowing.eq(nowShowing) : null;
    }

    private BooleanExpression idLt(Long lastId) {
        return lastId != null ? movie.id.lt(lastId) : null;
    }

}
