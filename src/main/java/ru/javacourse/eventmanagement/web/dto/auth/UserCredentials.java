package ru.javacourse.eventmanagement.web.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record UserCredentials(@NotBlank(message = "login must not be blank")
                              String login,
                              @NotBlank(message = "password must not be blank")
                              String password) {
}
