package com.cinema.moviebooking.scheduler;

import com.cinema.moviebooking.entity.ScreeningStatus;
import com.cinema.moviebooking.repository.screening.ScreeningRepository;
import com.cinema.moviebooking.util.QueryCounter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScreeningStatusScheduler {

    private final ScreeningRepository screeningRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final TransactionTemplate transactionTemplate;
    private final AtomicBoolean schedulerReadyFlag;

    private static final String SCREENING_TASK_KEY = "screening:tasks";
    private static final int BATCH_SIZE = 3000;

    /**
     * 상영 상태 자동 업데이트
     * - 예매 오픈(PENDING → SCHEDULED)
     * - 상영 시작(SCHEDULED → ONGOING)
     * - 상영 종료(ONGOING → COMPLETED)
     */
    @Scheduled(fixedRate = 60000)
    public void processScreeningTasks() {
        if (!schedulerReadyFlag.get()) {
            log.warn("AdminInitializer가 아직 완료되지 않았습니다. 스케줄러를 건너뜁니다.");
            return;
        }

        QueryCounter.start();
        long start = System.currentTimeMillis();
        long totalProcessed = 0L;
        boolean hasMoreTasks = true;
        long nowScore = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        try {
            while (hasMoreTasks) {
                Set<String> tasks = redisTemplate.opsForZSet().rangeByScore(
                        SCREENING_TASK_KEY, 0, nowScore, 0, BATCH_SIZE
                );

                if (tasks == null || tasks.isEmpty()) {
                    hasMoreTasks = false;
                    continue;
                }

                redisTemplate.opsForZSet().remove(SCREENING_TASK_KEY, tasks.toArray());
                Long processedCount = processBatchInTransaction(tasks);
                totalProcessed += processedCount;
            }

        } finally {
            long end = System.currentTimeMillis();
            long duration = end - start;
            Integer queryCount = QueryCounter.getCount();
            QueryCounter.end();
            if (totalProcessed > 0) {
                log.info("[스케줄러] 실행 완료: {}건 (총 소요시간: {} ms, 쿼리: {})",
                        totalProcessed, duration, queryCount);
            }
        }
    }

    private Long processBatchInTransaction(Set<String> tasks) {
        return transactionTemplate.execute(status -> {
            List<Long> openIds = new ArrayList<>();
            List<Long> startIds = new ArrayList<>();
            List<Long> endIds = new ArrayList<>();
            for (String task : tasks) {
                String[] parts = task.split(":");
                if (parts.length != 2) continue;
                Long screeningId = Long.parseLong(parts[0]);
                String action = parts[1];

                switch (action) {
                    case "OPEN":
                        openIds.add(screeningId);
                        break;
                    case "START":
                        startIds.add(screeningId);
                        break;
                    case "END":
                        endIds.add(screeningId);
                        break;
                }
            }
            long openCount = 0L;
            if (!openIds.isEmpty()) {
                openCount = screeningRepository.bulkUpdateStatus(openIds, ScreeningStatus.PENDING,
                        ScreeningStatus.SCHEDULED);
            }

            long startCount = 0L;
            if (!startIds.isEmpty()) {
                startCount = screeningRepository.bulkUpdateStatus(startIds, ScreeningStatus.SCHEDULED,
                        ScreeningStatus.ONGOING);
            }

            long endCount = 0L;
            if (!endIds.isEmpty()) {
                endCount = screeningRepository.bulkUpdateStatus(endIds, ScreeningStatus.ONGOING,
                        ScreeningStatus.COMPLETED);
            }
            return openCount + startCount + endCount;
        });
    }
}