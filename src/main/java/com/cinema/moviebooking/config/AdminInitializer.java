package com.cinema.moviebooking.config;

import com.cinema.moviebooking.entity.Member;
import com.cinema.moviebooking.entity.Role;
import com.cinema.moviebooking.entity.Screening;
import com.cinema.moviebooking.entity.ScreeningStatus;
import com.cinema.moviebooking.repository.member.MemberRepository;
import com.cinema.moviebooking.repository.screening.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private static final String SEAT_COUNT_KEY = "screening:seats";

    public AdminInitializer(MemberRepository memberRepository,
                            PasswordEncoder passwordEncoder,
                            ScreeningRepository screeningRepository,
                            RedisTemplate<String, String> redisTemplate,
                            @Lazy AdminInitializer self) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.screeningRepository = screeningRepository;
        this.redisTemplate = redisTemplate;
        this.self = self;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        self.createAdmin();
        self.populateRedisSeatCounters();
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
            final int BATCH_SIZE = 1000;
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
}
