package ru.javacourse.eventmanagement.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.javacourse.eventmanagement.domain.exeptions.NotFoundUser;
import ru.javacourse.eventmanagement.domain.users.User;
import ru.javacourse.eventmanagement.domain.users.UserMapper;
import ru.javacourse.eventmanagement.db.repository.UserRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;



    public User getUserByLogin(String username) {
        return userRepository.findByLogin(username)
                .map(userMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundUser("User with username : %s".formatted(username)));
    }


    @Transactional
    public User registerUser(@Valid User newUser) {
        var newUserEntity = userMapper.mapToEntity(newUser);
        var login = newUserEntity.getLogin();
        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("This login(%s) is already taken".formatted(login));
        }
        userRepository.save(newUserEntity);
        log.info("User  saved :{}", newUserEntity);
        return userMapper.mapFromEntity(newUserEntity);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(userMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundUser("User with id %s not found".formatted(userId)));
    }
}
