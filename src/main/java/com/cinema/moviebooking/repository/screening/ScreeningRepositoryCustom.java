package com.cinema.moviebooking.repository.screening;

import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.ScreeningStatus;
import com.cinema.moviebooking.entity.Theater;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepositoryCustom {
    boolean existsByTheaterAndTimeRange(Theater theater, LocalDateTime startTime, LocalDateTime endTime);

    List<Screening> findValidByCinemaAndDate(Long cinemaId, LocalDate screeningDate);

    List<Screening> findScreeningsForStatusUpdate(ScreeningStatus status, LocalDateTime now);

    void updateToOngoingIfStarted(LocalDateTime now);

    void updateToCompletedIfEnded(LocalDateTime now);
}
