package com.cinema.moviebooking.repository;

import com.cinema.moviebooking.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
}
