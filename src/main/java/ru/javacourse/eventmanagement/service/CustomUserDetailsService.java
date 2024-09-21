package ru.javacourse.eventmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.javacourse.eventmanagement.domain.exeptions.NotFoundUser;
import ru.javacourse.eventmanagement.domain.users.CustomUserDetails;
import ru.javacourse.eventmanagement.domain.users.UserMapper;
import ru.javacourse.eventmanagement.repository.UserRepository;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .map(userMapper::mapFromEntity)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new NotFoundUser("User with login:%s not found".formatted(username)));
    }
}
