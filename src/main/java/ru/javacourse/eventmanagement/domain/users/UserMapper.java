package ru.javacourse.eventmanagement.domain.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javacourse.eventmanagement.entity.user.UserEntity;
import ru.javacourse.eventmanagement.web.dto.auth.UserRegistration;
import ru.javacourse.eventmanagement.web.dto.users.UserDto;


@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto mapToDtoFromEntity(UserEntity userEntity);

    User mapFromEntity(UserEntity entity);

    UserEntity mapToEntity(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "ROLE_USER")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userRegistration.password()))")
    User mapFromUserRegistration(UserRegistration userRegistration, PasswordEncoder passwordEncoder);


}


