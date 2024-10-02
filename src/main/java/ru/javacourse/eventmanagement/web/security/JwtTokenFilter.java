package ru.javacourse.eventmanagement.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.javacourse.eventmanagement.service.UserService;
import ru.javacourse.eventmanagement.service.security.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        var bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(bearerToken) || !bearerToken.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        bearerToken = bearerToken.substring(BEARER_PREFIX.length());
        var login = jwtService.extractUserLogin(bearerToken);
        if (StringUtils.isNoneEmpty(login) && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.isValidToken(bearerToken)) {
                var user = userService.getUserByLogin(login);
                var userDetails = new UserDetailsImpl(user);
                var context = SecurityContextHolder.createEmptyContext();
                var token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(token);
                SecurityContextHolder.setContext(context);
            } else {
                log.error("Jwt token for user with login {} is not valid", login);
                throw new BadCredentialsException("Jwt token for user with login " + login + " is not valid");
            }
        }
        filterChain.doFilter(request, response);
    }
}