package ru.javacourse.eventmanagement.domain.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javacourse.eventmanagement.db.entity.user.UserEntity;
import ru.javacourse.eventmanagement.web.dto.auth.UserRegistration;
import ru.javacourse.eventmanagement.web.dto.users.UserDto;


@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto mapToDto(User user);

    User mapFromEntity(UserEntity entity);

    UserEntity mapToEntity(User user);

}


