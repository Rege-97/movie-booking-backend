package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.cinema.*;
import com.cinema.moviebooking.entity.Cinema;
import com.cinema.moviebooking.exception.DuplicateResourceException;
import com.cinema.moviebooking.exception.NotFoundException;
import com.cinema.moviebooking.repository.cinema.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 영화관 관련 비즈니스 로직 처리
 * (영화관 등록, 조회, 수정, 삭제 등)
 */
@Service
@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    /**
     * 영화관 등록
     * - 영화관 이름 중복 체크 후 저장
     */
    @Transactional
    public CinemaCreateResponse createCinema(CinemaCreateRequest req) {
        if (cinemaRepository.existsByName(req.getName())) {
            throw new DuplicateResourceException("이미 존재하는 영화관 이름입니다.");
        }

        Cinema cinema = Cinema.builder()
                .name(req.getName())
                .region(req.getRegion())
                .address(req.getAddress())
                .contact(req.getContact())
                .build();

        cinemaRepository.save(cinema);

        return new CinemaCreateResponse(cinema.getId());
    }

    /**
     * 영화관 목록 조회 (커서 기반)
     * - 검색어(keyword) 및 지역(region) 필터 적용
     * - 마지막 ID(lastId)를 기준으로 다음 데이터 조회
     * - 조회 결과 크기(size)에 따라 다음 페이지 존재 여부(hasNext) 계산
     * - 응답 데이터에 nextCursor 포함 (다음 요청 시 기준점)
     */
    @Transactional(readOnly = true)
    public CinemaCursorResponse getCinemasByCursor(String keyword, String region, Long lastId, int size) {
        List<Cinema> cinemas = cinemaRepository.findByCursor(keyword, region, lastId, size + 1);
        boolean hasNext = cinemas.size() > size;

        List<CinemaResponse> responses = cinemas.stream()
                .limit(size)
                .map(CinemaResponse::from)
                .collect(Collectors.toCollection(ArrayList::new));

        Long nextCursor = (hasNext && !responses.isEmpty())
                ? responses.get(responses.size() - 1).getId()
                : null;

        return new CinemaCursorResponse(responses, nextCursor, hasNext);
    }

    /**
     * 영화관 상세 조회
     * - 영화관 존재 검증 후 반환
     */
    @Transactional(readOnly = true)
    public CinemaResponse getCinemaById(Long id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 영화관을 찾을 수 없습니다."));

        return CinemaResponse.from(cinema);
    }

    /**
     * 영화관 수정
     * - 영화관 존재 검증
     * - 영화관 정보 수정 후 반환
     */
    @Transactional
    public CinemaResponse updateCinema(Long id, CinemaUpdateRequest req) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 영화관을 찾을 수 없습니다."));

        cinema.updateInfo(req.getName(), req.getRegion(), req.getAddress(), req.getContact());

        return CinemaResponse.from(cinema);
    }

    /**
     * 영화관 삭제
     * - 영화관 존재 여부 검증
     * - 삭제 성공 시 트랜잭션 커밋
     */
    @Transactional
    public void deleteCinema(Long id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 영화관을 찾을 수 없습니다."));

        cinemaRepository.delete(cinema);
    }
}
