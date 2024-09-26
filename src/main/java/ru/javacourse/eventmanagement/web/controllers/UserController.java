package ru.javacourse.eventmanagement.web.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javacourse.eventmanagement.domain.users.UserMapper;
import ru.javacourse.eventmanagement.service.UserService;
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
    private final UserRegistrationMapper userRegistrationMapper;

    @PostMapping()
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid UserRegistration userRegistration) {
        log.info("Start of registration user with login: {}", userRegistration.login());
        var newUser = userRegistrationMapper.mapFromUserRegistration(userRegistration);
        var createdUser = userService.registerUser(newUser);
        log.info("User registration was successful with login: {} ,it is assigned an ID: {}", userRegistration.login(), createdUser.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.mapToDto(createdUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable @PositiveOrZero(message = "Id must be positive or equal to 0") Long userId) {
        log.info("A request was received to obtain a user by his ID {}", userId);
        var userById = userService.getUserById(userId);
        log.info("User with ID {} was successfully obtained", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.mapToDto(userById));
    }


}
