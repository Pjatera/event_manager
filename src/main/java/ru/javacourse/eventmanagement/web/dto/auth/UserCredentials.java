package ru.javacourse.eventmanagement.web.dto.auth;

import jakarta.validation.constraints.NotNull;

public record UserCredentials(@NotNull String login, @NotNull String password) {
}
