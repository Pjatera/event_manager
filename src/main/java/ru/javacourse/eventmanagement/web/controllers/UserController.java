package ru.javacourse.eventmanagement.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javacourse.eventmanagement.domain.users.UserMapper;
import ru.javacourse.eventmanagement.service.AuthenticationService;
import ru.javacourse.eventmanagement.service.UserService;
import ru.javacourse.eventmanagement.web.dto.auth.JwtResponse;
import ru.javacourse.eventmanagement.web.dto.auth.UserCredentials;
import ru.javacourse.eventmanagement.web.dto.auth.UserRegistration;
import ru.javacourse.eventmanagement.web.dto.auth.UserRegistrationMapper;
import ru.javacourse.eventmanagement.web.dto.users.UserDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;
    private final UserRegistrationMapper userRegistrationMapper;

    @PostMapping()
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRegistration userRegistration) {
        log.info("Start of registration user with login: {}", userRegistration.login());
        var newUser = userRegistrationMapper.mapFromUserRegistration(userRegistration);
        var createdUser = userService.registerUser(newUser);
        log.info("User registration was successful with login: {} ,it is assigned an ID: {}", userRegistration.login(), createdUser.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.mapToDto(createdUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        log.info("A request was received to obtain a user by his ID {}", userId);
        var userById = userService.getUserById(userId);
        log.info("User with ID {} was successfully obtained", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.mapToDto(userById));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody UserCredentials userCredentials) {
        log.info("Get request for authenticate user {} ", userCredentials.login());
        JwtResponse jwtToken = authenticationService.authenticateUser(userCredentials);
        log.info("User {} was successfully authenticated", jwtToken.token());
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }


}
