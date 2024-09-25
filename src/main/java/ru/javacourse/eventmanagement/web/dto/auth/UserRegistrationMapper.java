package ru.javacourse.eventmanagement.web.dto.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.javacourse.eventmanagement.domain.users.Role;
import ru.javacourse.eventmanagement.domain.users.User;

@Component
@RequiredArgsConstructor
public class UserRegistrationMapper {
    private final PasswordEncoder passwordEncoder;


    public User mapFromUserRegistration(@Valid UserRegistration userRegistration) {

        return new User(
                null,
                userRegistration.login(),
                passwordEncoder.encode(userRegistration.password()),
                userRegistration.age(),
                Role.ROLE_USER
        );
    }
}

