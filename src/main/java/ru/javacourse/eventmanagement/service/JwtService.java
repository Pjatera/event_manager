package ru.javacourse.eventmanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.javacourse.eventmanagement.domain.users.User;
import ru.javacourse.eventmanagement.web.dto.auth.JwtResponse;
import ru.javacourse.eventmanagement.web.dto.auth.UserCredentials;

@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;
    private final UserService userService;

    public JwtService(UserService userService) {
        this.userService = userService;    }

    public JwtResponse authenticateUser(UserCredentials userCredentials) {
        var loginFromUserCredentials = userCredentials.login();
        var passwordFromUserCredentials = userCredentials.password();
        var byLogin = userService.getByLogin(loginFromUserCredentials);
        if (!byLogin.getPassword().equals(passwordFromUserCredentials)) {
            throw new IllegalArgumentException("Неверный пароль");
        }
        return new JwtResponse("token");
    }

//    public String extractUserName(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }


}
