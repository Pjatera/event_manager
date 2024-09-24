package ru.javacourse.eventmanagement.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.javacourse.eventmanagement.service.UserService;
import ru.javacourse.eventmanagement.service.properties.JwtProperties;
import ru.javacourse.eventmanagement.web.dto.auth.JwtResponse;
import ru.javacourse.eventmanagement.web.dto.auth.UserCredentials;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }


    public JwtResponse authenticateUser(UserCredentials userCredentials) {
        var loginFromUserCredentials = userCredentials.login();
        var passwordFromUserCredentials = userCredentials.password();
        var user = userService.findUserByLogin(loginFromUserCredentials);
        if (!user.password().equals(passwordFromUserCredentials)) {
            throw new IllegalArgumentException("Неверный пароль");
        }
        return new JwtResponse(generateAccessToken(new JwtUserDetails(user), user.id()));
    }


    public boolean isValidToken(String token, UserDetails userDetails) {
        var claims = getClaims(token);
        String login = claims.getSubject();
        return claims.getExpiration().after(new Date(System.currentTimeMillis())) && login.equals(userDetails.getUsername());
    }
    public boolean isTokenExpired(String token) {
        var claims = getClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }


    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private Long getIdWithToken(String token) {
        var claims = getClaims(token);
        String id = claims.get("id").toString();
        return Long.valueOf(id);
    }

    public String generateAccessToken(UserDetails userDetails, Long userId) {
        Claims claims = Jwts
                .claims()
                .subject(userDetails.getUsername())
                .build();
        claims.put("id", userId);
        claims.put("roles", userDetails.getAuthorities());
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccess()))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        var claims = getClaims(token);
        String login = claims.getSubject();
        var userDetails = userDetailsService.loadUserByUsername(login);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());


    }

    public String extractUserName(String token) {
        var claims = getClaims(token);
        return claims.getSubject();
    }
}
