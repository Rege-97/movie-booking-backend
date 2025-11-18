package com.cinema.moviebooking.config;

import com.cinema.moviebooking.entity.Member;
import com.cinema.moviebooking.entity.Role;
import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.ScreeningStatus;
import com.cinema.moviebooking.repository.member.MemberRepository;
import com.cinema.moviebooking.repository.screening.ScreeningRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * 시스템 최초 실행 시 최고 관리자 계정 자동 생성
 */
@Component
@Slf4j
public class AdminInitializer {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ScreeningRepository screeningRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final AdminInitializer self;
    private final AtomicBoolean schedulerReadyFlag;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private static final String SEAT_COUNT_KEY = "screening:seats";
    private static final String SCREENING_TASK_KEY = "screening:tasks";

    private static final int BATCH_SIZE = 5000;

    public AdminInitializer(MemberRepository memberRepository,
                            PasswordEncoder passwordEncoder,
                            ScreeningRepository screeningRepository,
                            RedisTemplate<String, String> redisTemplate,
                            @Lazy AdminInitializer self,
                            AtomicBoolean schedulerReadyFlag) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.screeningRepository = screeningRepository;
        this.redisTemplate = redisTemplate;
        this.self = self;
        this.schedulerReadyFlag = schedulerReadyFlag;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        self.createAdmin();
        self.populateRedisSeatCounters();
        self.populateRedisScreeningTasks();
    }

    @Transactional
    public void createAdmin() {

        // 이미 관리자 계정이 있으면 생성하지 않음
        if (memberRepository.existsByEmail(adminEmail)) {
            return;
        }

        Member admin = Member.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .name("Admin")
                .role(Role.ROLE_ADMIN)
                .build();

        memberRepository.save(admin);
    }

    @Transactional(readOnly = true)
    public void populateRedisSeatCounters() {
        log.info("Redis 좌석 카운터 동기화 시작...");

        redisTemplate.delete(SEAT_COUNT_KEY);
        log.warn("기존 Redis 좌석 카운터({})를 삭제했습니다.", SEAT_COUNT_KEY);

        try (Stream<Screening> screenings = screeningRepository.streamAllByPendingOrScheduled()) {
            final Map<String, String> batchMap = new HashMap<>(BATCH_SIZE);
            final int[] totalCount = {0};

            screenings.forEach(screening -> {
                String screeningIdStr = screening.getId().toString();

                batchMap.put(screeningIdStr, screening.getAvailableSeats().toString());
                totalCount[0]++;

                if (batchMap.size() >= BATCH_SIZE) {
                    redisTemplate.opsForHash().putAll(SEAT_COUNT_KEY, batchMap);
                    batchMap.clear();
                }
            });

            if (!batchMap.isEmpty()) {
                redisTemplate.opsForHash().putAll(SEAT_COUNT_KEY, batchMap);
            }

            log.info("Redis에 {}개의 좌석 카운터를 새로 등록했습니다.", totalCount[0]);

        }
    }

    @Transactional(readOnly = true)
    public void populateRedisScreeningTasks() {
        log.info("Redis 스케줄러 작업 동기화 시작...");

        redisTemplate.delete(SCREENING_TASK_KEY);
        log.warn("기존 Redis 스케줄러 작업({})을 삭제했습니다.", SCREENING_TASK_KEY);

        final Set<ZSetOperations.TypedTuple<String>> batchTasks = new HashSet<>(5000); // 5000개 단위
        final int[] totalCount = {0};

        try (Stream<Screening> screenings = screeningRepository.streamAllActive()) {
            screenings.forEach(screening -> {
                String screeningId = screening.getId().toString();
                ScreeningStatus status = screening.getStatus();

                double openTimeScore = (double) screening.getOpenTime().toEpochSecond(ZoneOffset.UTC);
                double startTimeScore = (double) screening.getStartTime().toEpochSecond(ZoneOffset.UTC);
                double endTimeScore = (double) screening.getEndTime().toEpochSecond(ZoneOffset.UTC);

                switch (status) {
                    case PENDING:
                        batchTasks.add(new DefaultTypedTuple<>(screeningId + ":OPEN", openTimeScore));
                        batchTasks.add(new DefaultTypedTuple<>(screeningId + ":START", startTimeScore));
                        batchTasks.add(new DefaultTypedTuple<>(screeningId + ":END", endTimeScore));
                        totalCount[0] += 3;
                        break;
                    case SCHEDULED:
                        batchTasks.add(new DefaultTypedTuple<>(screeningId + ":START", startTimeScore));
                        batchTasks.add(new DefaultTypedTuple<>(screeningId + ":END", endTimeScore));
                        totalCount[0] += 2;
                        break;
                    case ONGOING:
                        batchTasks.add(new DefaultTypedTuple<>(screeningId + ":END", endTimeScore));
                        totalCount[0] += 1;
                        break;
                }

                if (batchTasks.size() >= BATCH_SIZE) {
                    redisTemplate.opsForZSet().add(SCREENING_TASK_KEY, batchTasks);
                    batchTasks.clear();
                }
            });
            if (!batchTasks.isEmpty()) {
                redisTemplate.opsForZSet().add(SCREENING_TASK_KEY, batchTasks);
            }
            log.info("Redis 스케줄러에 {}개의 상영 스케줄 작업을 새로 등록했습니다.", totalCount[0]);
            schedulerReadyFlag.set(true);
        } catch (Exception e) {
            log.error("Redis 스케줄러 작업 동기화 중 오류 발생", e);
        }
    }
}

