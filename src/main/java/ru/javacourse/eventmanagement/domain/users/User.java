package ru.javacourse.eventmanagement.domain.users;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class User {
    private final Long id;
    @NotNull
    private final String login;
    @NotNull
    private final String password;
    @Min(0)
    private final int age;
    @Setter
    private Role role;
}
