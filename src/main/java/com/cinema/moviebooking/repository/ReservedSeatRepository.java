package com.cinema.moviebooking.repository;

import com.cinema.moviebooking.entity.ReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {
}
