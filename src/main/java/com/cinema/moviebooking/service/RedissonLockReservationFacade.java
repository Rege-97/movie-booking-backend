package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.reservation.ReservationCreateRequest;
import com.cinema.moviebooking.dto.reservation.ReservationCreateResponse;
import com.cinema.moviebooking.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockReservationFacade {

    private final RedissonClient redissonClient;
    private final ReservationService reservationService;

    private String getLockKey(Long screeningId, Long seatId) {
        return "lock:reservation:screening:" + screeningId + ":seat:" + seatId;
    }

    public ReservationCreateResponse createReservation(Member member, ReservationCreateRequest req) {
        List<RLock> locks = req.getSeatIdList().stream()
                .map(seatId -> redissonClient.getLock(getLockKey(req.getScreeningId(), seatId)))
                .collect(Collectors.toList());

        RLock multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[0]));

        try {
            // tryLock(waitTime, leaseTime, unit)
            // waitTime: 락을 획득하기 위해 기다리는 시간 (10초)
            // leaseTime: 락을 획득한 후 점유하는 시간 (3초) - 이 시간이 지나면 강제 해제 (데드락 방지)
            boolean available = multiLock.tryLock(10, 3, TimeUnit.SECONDS);

            if (!available) {
                log.warn("락 획득 실패 (대기 시간 초과). 요청 좌석 수: {}, Screening ID: {}",
                        req.getSeatIdList().size(), req.getScreeningId());
                throw new RuntimeException("현재 예매 요청이 너무 많아 처리할 수 없습니다. 잠시 후 다시 시도해주세요.");
            }

            return reservationService.createReservation(member, req);
        } catch (InterruptedException e) {
            throw new RuntimeException("서버 오류가 발생했습니다.", e);
        } finally {
            // 락 해제 (현재 스레드가 락을 가지고 있을 때만 해제)
            for (RLock lock : locks) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }
}
