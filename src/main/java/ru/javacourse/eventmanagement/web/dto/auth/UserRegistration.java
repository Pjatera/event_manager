package ru.javacourse.eventmanagement.web.dto.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserRegistration {
    @NotNull
    @Size(min = 5, max = 255, message = "Login length must be smaller than 255 symbols")
    private final String login;
    @NotNull
    @Size(min = 5, max = 255, message = "Password length must be smaller than 255 symbols")
    private final String password;
    @NotNull
    @Min(value = 18, message = "User age must be more than 18 ")
    private final Integer age;
}
