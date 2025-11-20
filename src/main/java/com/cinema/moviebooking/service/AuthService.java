package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.auth.LoginRequest;
import com.cinema.moviebooking.dto.auth.LoginResponse;
import com.cinema.moviebooking.dto.auth.SignUpRequest;
import com.cinema.moviebooking.dto.auth.SignUpResponse;
import com.cinema.moviebooking.entity.Member;
import com.cinema.moviebooking.entity.Role;
import com.cinema.moviebooking.exception.DuplicateResourceException;
import com.cinema.moviebooking.exception.InvalidCredentialsException;
import com.cinema.moviebooking.exception.NotFoundException;
import com.cinema.moviebooking.repository.member.MemberRepository;
import com.cinema.moviebooking.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

/**
 * 인증 관련 비즈니스 로직 처리
 * (회원가입, 로그인, 토큰 갱신 등)
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    private String getRefreshTokenKey(Long memberId) {
        return REFRESH_TOKEN_PREFIX + memberId;
    }

    /**
     * 회원가입
     * - 이메일 중복 체크
     * - 비밀번호 암호화 후 저장
     */
    @Transactional
    public SignUpResponse signUp(SignUpRequest req) {
        if (memberRepository.existsByEmailAndDeletedAtIsNull(req.getEmail())) {
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
        }

        Member member = Member.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .role(Role.ROLE_USER)
                .build();

        memberRepository.save(member);
        return new SignUpResponse(member.getId());
    }

    /**
     * 로그인
     * - 사용자 검증 후 JWT 발급
     * - Refresh 토큰 DB 업데이트
     */
    @Transactional
    public LoginResponse login(LoginRequest req) {
        Member member = memberRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("아이디 또는 비밀번호가 틀렸습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new InvalidCredentialsException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        // 토큰 발급
        String accessToken = jwtTokenProvider.generateAccessToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);

        String key = getRefreshTokenKey(member.getId());
        Duration expiration = Duration.ofMillis(jwtTokenProvider.getRefreshExpirationMs());

        redisTemplate.opsForValue().set(key, refreshToken, expiration);

        return LoginResponse.from(member, accessToken, refreshToken);
    }

    /**
     * 로그아웃
     * - 사용자 검증 후 Refresh 토큰 제거
     */
    @Transactional
    public void logout(Long memberId, String accessToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원"));

        String key = getRefreshTokenKey(memberId);
        redisTemplate.delete(key);

        Duration expiration = Duration.ofMillis(jwtTokenProvider.getAccessExpirationMs());
        String blacklistKey = BLACKLIST_PREFIX + accessToken;

        redisTemplate.opsForValue().set(blacklistKey, "logout", expiration);
    }
}
