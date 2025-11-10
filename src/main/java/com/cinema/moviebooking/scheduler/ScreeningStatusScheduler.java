package com.cinema.moviebooking.scheduler;

import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.ScreeningStatus;
import com.cinema.moviebooking.repository.screening.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScreeningStatusScheduler {

    private final ScreeningRepository screeningRepository;

    @Scheduled(fixedRate = 60000)
    public void updateScreeningStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Screening> startingScreenings =
                screeningRepository.findScreeningsForStatusUpdate(ScreeningStatus.SCHEDULED, now);
        screeningRepository.updateToOngoingIfStarted(now);
        for (Screening screening : startingScreenings) {
            log.info("상영 시작: [{} / {}] | 영화: [{}] ({} ~ {})",
                    screening.getTheater().getCinema().getName(),
                    screening.getTheater().getName(),
                    screening.getMovie().getTitle(),
                    screening.getStartTime(),
                    screening.getEndTime()
            );
        }

        List<Screening> endingScreenings = screeningRepository.findScreeningsForStatusUpdate(ScreeningStatus.ONGOING,
                now);
        screeningRepository.updateToCompletedIfEnded(now);
        for (Screening screening : endingScreenings) {
            log.info("상영 종료: [{} / {}] | 영화: [{}] ({} ~ {})",
                    screening.getTheater().getCinema().getName(),
                    screening.getTheater().getName(),
                    screening.getMovie().getTitle(),
                    screening.getStartTime(),
                    screening.getEndTime()
            );
        }
    }
}
