package com.cinema.moviebooking.repository;

import com.cinema.moviebooking.entity.Cinema;
import com.cinema.moviebooking.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

    boolean existsByCinemaAndName(Cinema cinema, String name);
}
