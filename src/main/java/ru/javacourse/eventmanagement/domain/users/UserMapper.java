package ru.javacourse.eventmanagement.domain.users;

import org.mapstruct.Mapper;
import ru.javacourse.eventmanagement.entity.user.UserEntity;
import ru.javacourse.eventmanagement.web.dto.users.UserDto;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto mapToDto(User user);

    User mapFromDto(UserDto userDto);

    User mapFromEntity(UserEntity entity);

    UserEntity mapToEntity(User user);

}
