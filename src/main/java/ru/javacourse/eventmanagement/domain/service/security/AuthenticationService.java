package ru.javacourse.eventmanagement.domain.service.security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.javacourse.eventmanagement.domain.service.UserService;
import ru.javacourse.eventmanagement.web.dto.auth.JwtResponse;
import ru.javacourse.eventmanagement.web.dto.auth.UserCredentials;
import ru.javacourse.eventmanagement.web.security.UserDetailsImpl;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;


    public JwtResponse authenticateUser(@Valid UserCredentials userCredentials) {
        var loginFromUserCredentials = userCredentials.login();
        var passwordFromUserCredentials = userCredentials.password();
        if (!userService.isExistsByLogin(loginFromUserCredentials)) {
            throw new BadCredentialsException("User with login %s not found".formatted(loginFromUserCredentials));
        }
        var user = userService.getUserByLogin(loginFromUserCredentials);
        if (!passwordEncoder.matches(passwordFromUserCredentials, user.password())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new JwtResponse(jwtService.generateAccessToken(new UserDetailsImpl(user), user.id()));
    }

    public UserDetailsImpl getCurrentUserAuthenticated() {
        var authentication = getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    public Collection<? extends GrantedAuthority> getAuthoritiesCurrentUserAuthenticated() {
        var authentication = getAuthentication();
        return authentication.getAuthorities();
    }

    private Authentication getAuthentication() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.error("Authentication required");
            throw new BadCredentialsException("Authentication required");
        }
        return authentication;
    }

}
