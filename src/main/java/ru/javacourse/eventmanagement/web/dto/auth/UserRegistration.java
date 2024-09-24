package ru.javacourse.eventmanagement.web.dto.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record UserRegistration(
        @NotNull
        @Size(min = 5, max = 255, message = "Login length must be smaller than 255 symbols")
        String login,
        @NotNull
        @Size(min = 5, max = 255, message = "Password length must be smaller than 255 symbols")
        String password,
        @NotNull
        @Min(value = 18, message = "User age must be more than 18 ")
        Integer age) {
}
