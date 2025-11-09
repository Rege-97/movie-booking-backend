package com.cinema.moviebooking.repository;

import com.cinema.moviebooking.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<Cinema, Long>, CinemaRepositoryCustom {
    boolean existsByName(String name);
}
