package com.cinema.moviebooking.repository.screening;

import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.ScreeningStatus;
import com.cinema.moviebooking.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface ScreeningRepository extends JpaRepository<Screening, Long>, ScreeningRepositoryCustom {
    List<Screening> findByTheater(Theater theater);

    @Query("SELECT s FROM Screening s WHERE s.status = 'PENDING' OR s.status = 'SCHEDULED'")
    Stream<Screening> streamAllByPendingOrScheduled();

    @Query("SELECT s FROM Screening s WHERE s.status IN (com.cinema.moviebooking.entity.ScreeningStatus.PENDING, com" +
            ".cinema.moviebooking.entity.ScreeningStatus.SCHEDULED, com.cinema.moviebooking.entity.ScreeningStatus" +
            ".ONGOING)")
    Stream<Screening> streamAllActive();

}
