package ru.javacourse.eventmanagement.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javacourse.eventmanagement.domain.users.UsersMapper;
import ru.javacourse.eventmanagement.service.UserService;
import ru.javacourse.eventmanagement.web.dto.users.UserDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UsersMapper userMapper;

    @PostMapping()
    public ResponseEntity<UserDto> createUser(UserDto newUserDto){
        var newUser = userMapper.mapFromDto(newUserDto);
        var createdUser = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.mapToDto(createdUser));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId){
        var userById = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.mapToDto(userById));
    }

    @PostMapping("/auth")
    public ResponseEntity<> authenticateUser(@RequestBody UserDto userDto){

        return null;
    }


}
