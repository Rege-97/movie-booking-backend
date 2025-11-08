package com.cinema.moviebooking.controller;

import com.cinema.moviebooking.common.response.ApiResponse;
import com.cinema.moviebooking.dto.auth.LoginRequest;
import com.cinema.moviebooking.dto.auth.LoginResponse;
import com.cinema.moviebooking.dto.auth.SignUpRequest;
import com.cinema.moviebooking.dto.auth.SignUpResponse;
import com.cinema.moviebooking.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        SignUpResponse signUpResponse = authService.signUp(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(signUpResponse, "회원가입이 완료되었습니다."));
    }

    /**
     * 로그인 요청 처리
     * - 사용자 검증 및 JWT 발급
     * - 로그인 성공 시 200(OK) 반환
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        LoginResponse loginResponse = authService.login(req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(loginResponse, "로그인 되었습니다."));
    }
}
