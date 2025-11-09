package com.cinema.moviebooking.security;

import com.cinema.moviebooking.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.access-expiration-ms}")
    private long accessExpirationMs;

    @Getter
    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    private SecretKey secretKey;
    private static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;

    @PostConstruct
    void init() {
        // HMAC-SHA 키 생성
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Member member) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpirationMs);

        return Jwts.builder()
                .subject(member.getEmail()) // 주체: 이메일 or ID
                .claim("id", member.getId())
                .claim("role", member.getRole())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey, ALGORITHM)
                .compact();
    }

    public String generateRefreshToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshExpirationMs);

        return Jwts.builder()
                .subject(member.getEmail())
                .claim("id", member.getId())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, ALGORITHM)
                .compact();
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
}
