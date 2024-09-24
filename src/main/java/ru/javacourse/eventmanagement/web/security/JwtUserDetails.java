package ru.javacourse.eventmanagement.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.javacourse.eventmanagement.domain.users.User;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JwtUserDetails implements UserDetails {
    private final User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.role().name()));
    }

    @Override
    public String getPassword() {
        return user.password();
    }

    @Override
    public String getUsername() {
        return user.login();
    }

}
