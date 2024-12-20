package ru.javacourse.eventmanagement.web.dto.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record UserRegistration(
        @NotBlank
        @Size(min = 5, max = 255, message = "Login length must be smaller than 255 symbols and must be more than 5 symbols")
        String login,
        @NotBlank
        @Size(min = 5, max = 255, message = "Password length must be smaller than 255 symbols and must be more than 5 symbols")
        String password,
        @NotNull
        @Min(value = 18, message = "User age must be more than 18 ")
        Integer age) {
}
