package ru.javacourse.eventmanagement.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javacourse.eventmanagement.service.security.AuthenticationService;
import ru.javacourse.eventmanagement.web.dto.auth.JwtResponse;
import ru.javacourse.eventmanagement.web.dto.auth.UserCredentials;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationService authenticationService;


    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody @Valid UserCredentials userCredentials) {
        log.info("Get request for authenticate user {} ", userCredentials.login());
        JwtResponse jwtToken = authenticationService.authenticateUser(userCredentials);
        log.info("User {} was successfully authenticated", jwtToken.token());
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }
}
