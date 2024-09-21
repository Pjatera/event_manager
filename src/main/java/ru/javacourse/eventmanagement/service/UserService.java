package ru.javacourse.eventmanagement.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.javacourse.eventmanagement.domain.exeptions.NotFoundUser;
import ru.javacourse.eventmanagement.domain.users.Role;
import ru.javacourse.eventmanagement.domain.users.User;
import ru.javacourse.eventmanagement.domain.users.UserMapper;
import ru.javacourse.eventmanagement.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public User getByLogin(String username) {
        return userRepository.findByLogin(username)
                .map(userMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundUser("Пользователь не найден"));

    }

    public User getCurrentUser() {
        var login = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByLogin(login);
    }


    @Transactional
    public User createUser(@Valid User newUser) {
        newUser.setRole(Role.ROLE_USER);
        var newUserEntity = userMapper.mapToEntity(newUser);
        var login = newUserEntity.getLogin();
        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("This login(%s) is already taken".formatted(login));
        }
        userRepository.save(newUserEntity);
        return userMapper.mapFromEntity(newUserEntity);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(userMapper::mapFromEntity)
                .orElseThrow(() -> new NotFoundUser("User with id %s not found".formatted(userId)));
    }
}
