package ru.javacourse.eventmanagement.domain.users;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;


public record User(
        @Null(message = "For a new user, the ID must be empty")
        Long id,
        @NotNull
        @Size(min = 5, max = 255, message = "Login length must be smaller than 255 symbols")
        String login,
        @NotNull
        @Size(min = 5, max = 255, message = "Password length must be smaller than 255 symbols")
        String password,
        @Min(value = 18, message = "User age must be more than 18 ")
        Integer age,
        Role role) {
    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", login='" + login +
               ", age=" + age +
               ", role=" + role +
               '}';
    }

}
