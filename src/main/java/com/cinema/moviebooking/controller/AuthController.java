package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.auth.*;
import com.cinema.moviebooking.entity.Member;
import com.cinema.moviebooking.security.CustomUserDetails;
import com.cinema.moviebooking.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 인증 관련 요청을 처리하는 컨트롤러
 * (회원가입, 로그인 등)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 요청 처리
     * - 요청값 검증(@Valid)
     * - 회원가입 성공 시 201(CREATED) 반환
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest req) {
        SignUpResponse res = authService.signUp(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(res, "회원가입이 완료되었습니다."));
    }

    /**
     * 로그인 요청 처리
     * - 사용자 검증 및 JWT 발급
     * - 로그인 성공 시 200(OK) 반환
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        LoginResponse res = authService.login(req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "로그인 되었습니다."));
    }

    /**
     * 로그아웃 요청 처리
     * - Refresh Token 제거를 통한 재발급 차단
     * - 로그아웃 성공 시 204(No Content) 반환
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal Member member,
                                    @RequestHeader("Authorization") String authorizationHeader) {
        authService.logout(member.getId(), authorizationHeader.substring(7));
        return ResponseEntity.noContent().build();
    }

    /**
     * Access Token 재발급 요청 처리 (Refresh Token 사용)
     * - 요청 바디에서 Refresh Token 추출
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequest req) {
        LoginResponse res = authService.reissueTokens(req.getRefreshToken());
        return ResponseEntity.ok()
                .body(ApiResponse.success(res, "Access Token이 재발급되었습니다."));
    }
}
