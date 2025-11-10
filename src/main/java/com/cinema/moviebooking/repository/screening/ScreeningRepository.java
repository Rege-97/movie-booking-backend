package com.cinema.moviebooking.repository.screening;

import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long>, ScreeningRepositoryCustom {
    List<Screening> findByTheater(Theater theater);
}
