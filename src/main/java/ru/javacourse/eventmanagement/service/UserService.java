package ru.javacourse.eventmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.javacourse.eventmanagement.db.entity.event.RegistrationEntity;
import ru.javacourse.eventmanagement.db.repository.UserRepository;
import ru.javacourse.eventmanagement.domain.event.Event;
import ru.javacourse.eventmanagement.domain.mapper.EventMapper;
import ru.javacourse.eventmanagement.domain.mapper.UserMapper;
import ru.javacourse.eventmanagement.domain.users.User;

import java.util.List;

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
                .map(userMapper::mapFromEntityToUser)
                .orElseThrow(() -> new EntityNotFoundException("User with username : %s".formatted(username)));
    }


    @Transactional
    public User registerUser(@Valid User newUser) {
        var newUserEntity = userMapper.mapFromUserToEntity(newUser);
        var login = newUserEntity.getLogin();
        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("This login(%s) is already taken".formatted(login));
        }
        userRepository.save(newUserEntity);
        log.info("User  saved :{}", newUserEntity);
        return userMapper.mapFromEntityToUser(newUserEntity);
    }

    public boolean isExistsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(userMapper::mapFromEntityToUser)
                .orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(userId)));
    }

}
