package com.cinema.moviebooking.repository.seat;

import com.cinema.moviebooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
