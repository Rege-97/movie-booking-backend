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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest req) {
        SignUpResponse signUpResponse = authService.signUp(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(signUpResponse, "회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        LoginResponse loginResponse = authService.login(req);
        return ResponseEntity.ok()
                .body(ApiResponse.success(loginResponse, "로그인 되었습니다."));
    }
}
