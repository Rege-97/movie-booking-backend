package com.cinema.moviebooking.repository;

import com.cinema.moviebooking.entity.Cinema;

import java.util.List;

public interface CinemaRepositoryCustom {
    List<Cinema> findByCursor(String keyword, String region, Long lastId, int size);
}
