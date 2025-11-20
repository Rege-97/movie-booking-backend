package com.cinema.moviebooking.repository.theater;

import com.cinema.moviebooking.entity.Cinema;
import com.cinema.moviebooking.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

    boolean existsByCinemaAndNameAndDeletedAtIsNull(Cinema cinema, String name);

    List<Theater> findByCinemaId(Long cinemaId);
}
