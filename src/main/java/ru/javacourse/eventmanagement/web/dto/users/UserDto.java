package ru.javacourse.eventmanagement.web.dto.users;

import ru.javacourse.eventmanagement.domain.users.Role;

public record UserDto(Long id,
                      String login,
                      Integer age,
                      Role role) {

}
