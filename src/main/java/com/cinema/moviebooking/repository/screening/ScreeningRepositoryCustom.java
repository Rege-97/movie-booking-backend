package com.cinema.moviebooking.repository.screening;

import com.cinema.moviebooking.entity.Theater;

import java.sql.Time;
import java.time.LocalDateTime;

public interface ScreeningRepositoryCustom {
    boolean existsByTheaterAndTimeRange(Theater theater, LocalDateTime startTime, LocalDateTime endTime);
}
