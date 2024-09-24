package ru.javacourse.eventmanagement.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javacourse.eventmanagement.domain.users.UserMapper;
import ru.javacourse.eventmanagement.web.dto.auth.UserRegistration;
import ru.javacourse.eventmanagement.web.security.JwtService;
import ru.javacourse.eventmanagement.service.UserService;
import ru.javacourse.eventmanagement.web.dto.auth.JwtResponse;
import ru.javacourse.eventmanagement.web.dto.auth.UserCredentials;
import ru.javacourse.eventmanagement.web.dto.users.UserDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping()
    public ResponseEntity<UserDto> registerUser( @RequestBody  UserRegistration userRegistration){
        log.info("Start of registration user with login: {}" , userRegistration.login());
        var newUser = userMapper.mapFromUserRegistration(userRegistration,passwordEncoder);
        var createdUser = userService.registerUser(newUser);
        log.info("User registration was successful with login: {} ,it is assigned an ID: {}",userRegistration.login(),createdUser.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId){
        var userById = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userById);
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody UserCredentials userCredentials){
        JwtResponse jwtToken = jwtService.authenticateUser(userCredentials);
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }


}
