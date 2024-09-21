package ru.javacourse.eventmanagement.web.dto.auth;

import jakarta.validation.constraints.NotNull;


public record JwtResponse(@NotNull String token) {
}
