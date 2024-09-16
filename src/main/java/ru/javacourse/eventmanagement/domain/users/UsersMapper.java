package ru.javacourse.eventmanagement.domain.users;

import org.springframework.stereotype.Component;
import ru.javacourse.eventmanagement.entity.user.UserEntity;
import ru.javacourse.eventmanagement.domain.Mapper;
import ru.javacourse.eventmanagement.web.dto.users.UserDto;

@Component
public class UsersMapper implements Mapper<UserDto, User, UserEntity> {


    @Override
    public UserDto mapToDto(User model) {
        return null;
    }

    @Override
    public User mapFromDto(UserDto dto) {
        return null;
    }

    @Override
    public User mapFromEntity(UserEntity entity) {
        return null;
    }

    @Override
    public UserEntity mapToEntity(User model) {
        return null;
    }
}
