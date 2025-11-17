package com.cinema.moviebooking.scheduler;

import com.cinema.moviebooking.repository.screening.ScreeningRepository;
import com.cinema.moviebooking.util.QueryCounter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScreeningStatusScheduler {

    private final ScreeningRepository screeningRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String SEAT_COUNT_KEY = "screening:seats";

    /**
     * 상영 상태 자동 업데이트
     * - 예매 오픈(PENDING → SCHEDULED)
     * - 상영 시작(SCHEDULED → ONGOING)
     * - 상영 종료(ONGOING → COMPLETED)
     */
    @Scheduled(initialDelay = 180000, fixedRate = 60000)
    @Transactional
    public void updateScreeningStatus() {

        QueryCounter.start();
        long start = System.currentTimeMillis();
        boolean isChanged = false;

        try {
            LocalDateTime now = LocalDateTime.now();

            Long openCount = screeningRepository.updateToScheduledIfOpenTimeReached(now);
            if (openCount > 0) {
                log.info("예매 오픈: {}건 처리 완료", openCount);
                isChanged = true;
            }

            Long startCount = screeningRepository.updateToOngoingIfStarted(now);
            if (startCount > 0) {
                log.info("상영 시작: {}건 처리 완료", startCount);
                isChanged = true;
            }

            Long endCount = screeningRepository.updateToCompletedIfEnded(now);
            if (endCount > 0) {
                log.info("상영 종료: {}건 처리 완료", endCount);
                isChanged = true;
            }

        } finally {
            long end = System.currentTimeMillis();
            long duration = end - start;
            Integer queryCount = QueryCounter.getCount();

            QueryCounter.end();

            if (queryCount != null) {
                log.info("[스케줄러] updateScreeningStatus - Query Count: {}", queryCount);
            }

            if (duration > 1000 || isChanged) {
                log.info("[스케줄러] 실행 완료 (총 소요시간: {} ms)", duration);
            }
        }
    }
}