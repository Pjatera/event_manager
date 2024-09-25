package ru.javacourse.eventmanagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.javacourse.eventmanagement.service.properties.JwtProperties;
import ru.javacourse.eventmanagement.web.dto.auth.JwtResponse;
import ru.javacourse.eventmanagement.web.dto.auth.UserCredentials;
import ru.javacourse.eventmanagement.web.security.JwtUserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final UserService userService;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }



    public boolean isValidToken(String token, UserDetails userDetails) {
        var claims = getClaims(token);
        String login = claims.getSubject();
        Instant expiration = claims.getExpiration().toInstant();
        Instant now = Instant.now();
        return expiration.isAfter(now) && login.equals(userDetails.getUsername());
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
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("id", userId)
                .claim("roles", userDetails.getAuthorities())
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
