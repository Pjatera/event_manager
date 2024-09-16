package ru.javacourse.eventmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javacourse.eventmanagement.domain.users.Role;
import ru.javacourse.eventmanagement.domain.users.User;
import ru.javacourse.eventmanagement.domain.users.UsersMapper;
import ru.javacourse.eventmanagement.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UsersMapper usersMapper;

    @Transactional
    public User createUser(User newUser) {
        newUser.setRole(Role.ROLE_USER);
        var newUserEntity = usersMapper.mapToEntity(newUser);
        var login = newUserEntity.getLogin();
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("This login(%s) is already taken".formatted(login));
        }
        return usersMapper.mapFromEntity(userRepository.save(newUserEntity));
    }
}
