package com.cinema.moviebooking.service;

import com.cinema.moviebooking.dto.auth.LoginRequest;
import com.cinema.moviebooking.dto.auth.LoginResponse;
import com.cinema.moviebooking.dto.auth.SignUpRequest;
import com.cinema.moviebooking.dto.auth.SignUpResponse;
import com.cinema.moviebooking.entity.Member;
import com.cinema.moviebooking.entity.Role;
import com.cinema.moviebooking.exception.DuplicateEmailException;
import com.cinema.moviebooking.exception.InvalidCredentialsException;
import com.cinema.moviebooking.repository.MemberRepository;
import com.cinema.moviebooking.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignUpResponse signUp(SignUpRequest req) {
        if (memberRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
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

    @Transactional
    public LoginResponse login(LoginRequest req) {
        Member member = memberRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("아이디 또는 비밀번호가 틀렸습니다."));

        if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new InvalidCredentialsException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);

        member.updateRefreshToken(refreshToken);

        return LoginResponse.from(member, accessToken, refreshToken);
    }
}
