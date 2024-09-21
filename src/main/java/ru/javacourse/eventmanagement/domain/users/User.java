package ru.javacourse.eventmanagement.domain.users;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Data
@Setter(AccessLevel.NONE)
public class User {
    @Null(message = "For a new user, the ID must be empty")
    private final Long id;
    @NotNull
    @Length(max = 255, message = "Login length must be smaller than 255 symbols")
    private final String login;
    @NotNull
    @Length(max = 255, message = "Password length must be smaller than 255 symbols")
    private final String password;
    @Min(value = 18, message = "User age must be more than 18 ")
    private final Integer age;
    @Setter
    private Role role;

}
