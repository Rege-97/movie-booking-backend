package com.cinema.moviebooking.repository.movie;

import com.cinema.moviebooking.entity.Movie;
import com.cinema.moviebooking.entity.Rating;

import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> findByCursor(String keyword, Rating rating, Boolean nowShowing,
                             Integer releaseYear, String searchBy, Long lastId,
                             int size);
}
