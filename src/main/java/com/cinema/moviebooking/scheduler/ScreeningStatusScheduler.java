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

        boolean isChanged = false;

        Long openCount = screeningRepository.updateToScheduledIfOpenTimeReached(now);
        if (openCount > 0) {
            log.info("예매 오픈: {}건 처리 완료", openCount);
            isChanged = true;
        }

        // 상영 시작 처리
        Long startCount = screeningRepository.updateToOngoingIfStarted(now);
        if (startCount > 0) {
            log.info("상영 시작: {}건 처리 완료", startCount);
            isChanged = true;
        }

        // 상영 종료 처리
        Long endCount = screeningRepository.updateToCompletedIfEnded(now);
        if (endCount > 0) {
            log.info("상영 종료: {}건 처리 완료", endCount);
            isChanged = true;
        }

        long end = System.currentTimeMillis();
        long duration = end - start;
        if (duration > 1000 || isChanged) {
            log.info("[스케줄러] 실행 완료 (총 소요시간: {} ms)", duration);
        }
    }
}
