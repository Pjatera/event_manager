package ru.javacourse.eventmanagement.web.dto.users;

import ru.javacourse.eventmanagement.domain.users.Role;

public record UserDto(Long id,
                      String login,
                      String password,
                      Integer age,
                      Role role) {

}
