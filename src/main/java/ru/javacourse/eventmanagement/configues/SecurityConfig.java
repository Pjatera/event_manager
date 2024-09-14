package ru.javacourse.eventmanagement.configues;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityConfigure(HttpSecurity http) {
        http .authorizeRequests()
                .anyRequest()
                .permitAll();



        return http.build();
    }

}
