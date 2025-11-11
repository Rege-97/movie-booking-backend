package com.cinema.moviebooking.scheduler;

import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.ScreeningStatus;
import com.cinema.moviebooking.repository.screening.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScreeningStatusScheduler {

    private final ScreeningRepository screeningRepository;

    /**
     * 상영 상태 자동 업데이트
     * - 예매 오픈(PENDING → SCHEDULED)
     * - 상영 시작(SCHEDULED → ONGOING)
     * - 상영 종료(ONGOING → COMPLETED)
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateScreeningStatus() {
        long start = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();

        // 예매 오픈 처리
        List<Screening> openingScreenings =
                screeningRepository.findScreeningsForStatusUpdate(ScreeningStatus.PENDING, now);
        screeningRepository.updateToScheduledIfOpenTimeReached(now);

        for (Screening screening : openingScreenings) {
            log.info("🎟예매 오픈: [{} / {}] | 영화: [{}] (오픈: {}, 상영: {})",
                    screening.getTheater().getCinema().getName(),
                    screening.getTheater().getName(),
                    screening.getMovie().getTitle(),
                    screening.getOpenTime(),
                    screening.getStartTime()
            );
        }

        // 상영 시작 처리
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

        // 상영 종료 처리
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
        long end = System.currentTimeMillis();
        log.info("[스케줄러] 실행 완료 (총 소요시간: {} ms)", (end - start));
    }
}
