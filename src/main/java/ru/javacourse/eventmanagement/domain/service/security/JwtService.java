package ru.javacourse.eventmanagement.domain.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.javacourse.eventmanagement.domain.service.security.properties.JwtProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }


    public boolean isValidToken(String token) {
        var claims = getClaims(token);
        Instant expiration = claims.getExpiration().toInstant();
        Instant now = Instant.now();
        return expiration.isAfter(now);
    }


    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String generateAccessToken(UserDetails userDetails, Long userId) {
        Instant nowDate = Instant.now();
        Instant expirationDate = nowDate.plus(Duration.ofMillis(jwtProperties.getAccess()));
        var collectAuthority = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("id", userId)
                .claim("roles", collectAuthority)
                .issuedAt(Date.from(nowDate))
                .expiration(Date.from(expirationDate))
                .signWith(key)
                .compact();
    }


    public String extractUserLogin(String token) {
        var claims = getClaims(token);
        return claims.getSubject();
    }


}