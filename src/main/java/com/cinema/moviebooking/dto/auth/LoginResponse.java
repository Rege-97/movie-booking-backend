package com.cinema.moviebooking.dto.auth;

import com.cinema.moviebooking.entity.Member;
import com.cinema.moviebooking.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private Long memberId;
    private String email;
    private String name;
    private Role role;
    private String accessToken;
    private String refreshToken;

    public static LoginResponse from(Member member, String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
