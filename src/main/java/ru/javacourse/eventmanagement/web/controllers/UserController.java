package ru.javacourse.eventmanagement.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javacourse.eventmanagement.domain.users.UserMapper;
import ru.javacourse.eventmanagement.service.JwtService;
import ru.javacourse.eventmanagement.service.UserService;
import ru.javacourse.eventmanagement.web.dto.auth.JwtResponse;
import ru.javacourse.eventmanagement.web.dto.auth.UserCredentials;
import ru.javacourse.eventmanagement.web.dto.users.UserDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto newUserDto){
        var newUser = userMapper.mapFromDto(newUserDto);
        var createdUser = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.mapToDto(createdUser));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId){
        var userById = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.mapToDto(userById));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody UserCredentials userCredentials){
        JwtResponse jwtToken = jwtService.authenticateUser(userCredentials);
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }


}
