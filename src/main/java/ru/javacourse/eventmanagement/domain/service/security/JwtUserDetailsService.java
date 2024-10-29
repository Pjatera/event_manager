package ru.javacourse.eventmanagement.domain.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.javacourse.eventmanagement.domain.service.UserService;
import ru.javacourse.eventmanagement.web.security.UserDetailsImpl;

@RequiredArgsConstructor
@Service
@Primary
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(userService.getUserByLogin(username));
    }
}