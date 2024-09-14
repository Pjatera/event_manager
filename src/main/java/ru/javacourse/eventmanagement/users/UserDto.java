package ru.javacourse.eventmanagement.users;

public record UserDto(Long id,
                      String login,
                      String password,
                      int age,
                      Role role) {

}
