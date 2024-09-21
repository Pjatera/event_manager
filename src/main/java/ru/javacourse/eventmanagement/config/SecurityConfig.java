package ru.javacourse.eventmanagement.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import ru.javacourse.eventmanagement.domain.users.User;
import ru.javacourse.eventmanagement.service.UserService;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApplicationContext applicationContext;
    private final UserService userService;


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }



//    @Bean
//    @SneakyThrows
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
//        return configuration.getAuthenticationManager();
//
//
//    }
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userService.userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityConfigure(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()).
                authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users","/users/auth").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**","/openapi.yaml").permitAll()
                        .requestMatchers("/locations/**","/users/{userId}").hasRole("ADMIN")
                        .anyRequest().authenticated());

               // .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));
//                .authenticationProvider(authenticationProvider());
               // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }





}
