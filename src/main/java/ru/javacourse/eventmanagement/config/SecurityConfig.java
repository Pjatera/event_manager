package ru.javacourse.eventmanagement.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.javacourse.eventmanagement.web.security.AccessCustomDeniedHandler;
import ru.javacourse.eventmanagement.web.security.AuthenticationCustomEntryPoint;
import ru.javacourse.eventmanagement.web.security.JwtTokenFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationCustomEntryPoint authenticationCustomEntryPoint;
    private final AccessCustomDeniedHandler accessCustomDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();


    }



    @Bean
    @SneakyThrows
    public SecurityFilterChain securityConfigure(HttpSecurity http) {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .exceptionHandling(auth -> auth
                        .authenticationEntryPoint(authenticationCustomEntryPoint)
                        .accessDeniedHandler(accessCustomDeniedHandler))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/users", "/users/auth").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/*").hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/locations").hasAnyRole(ROLE_ADMIN,ROLE_USER)
                        .requestMatchers(HttpMethod.POST, "/locations").hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/locations/*").hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/locations/*").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                        .requestMatchers(HttpMethod.PUT, "/locations/*").hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.POST, "/events").hasRole(ROLE_USER)
                        .requestMatchers(HttpMethod.DELETE, "/events/*").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                        .requestMatchers(HttpMethod.GET, "/events/*").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                        .requestMatchers(HttpMethod.PUT, "/events/*").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                        .requestMatchers("/events/**").hasRole(ROLE_USER)
                        .requestMatchers(HttpMethod.POST, "/events/search").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                        .requestMatchers("/css/**",
                                "/js/**",
                                "/img/**",
                                "/lib/**",
                                "/favicon.ico",
                                "/swagger-ui/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/configuration/ui",
                                "/swagger-resources/**",
                                "/configuration/security",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/v3/api-docs/swagger-config",
                                "/openapi.yaml").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .anonymous(Customizer.withDefaults())
                .headers(Customizer.withDefaults())
                .build();
    }


}
