package ru.javacourse.eventmanagement.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.javacourse.eventmanagement.web.dto.auth.JwtResponse;
import ru.javacourse.eventmanagement.web.dto.auth.UserCredentials;
import ru.javacourse.eventmanagement.web.security.JwtUserDetails;

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
        return new JwtResponse(jwtService.generateAccessToken(new JwtUserDetails(user), user.id()));
    }

}
