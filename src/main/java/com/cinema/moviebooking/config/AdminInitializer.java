package com.cinema.moviebooking.config;

import com.cinema.moviebooking.entity.Member;
import com.cinema.moviebooking.entity.Role;
import com.cinema.moviebooking.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 시스템 최초 실행 시 최고 관리자 계정 자동 생성
 */
@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @EventListener(ApplicationReadyEvent.class)
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
}
