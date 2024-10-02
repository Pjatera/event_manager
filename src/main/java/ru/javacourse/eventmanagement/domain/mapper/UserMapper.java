package ru.javacourse.eventmanagement.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.javacourse.eventmanagement.db.entity.user.UserEntity;
import ru.javacourse.eventmanagement.domain.users.User;
import ru.javacourse.eventmanagement.web.dto.users.UserDto;

import java.util.HashSet;


@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {HashSet.class})
public interface UserMapper {

    UserDto mapFromUserToDto(User user);

    User mapFromEntityToUser(UserEntity entity);

    @Mapping(target = "events", expression = "java(new HashSet<>())")
    @Mapping(target = "eventsRegistrationEntities", expression = "java(new HashSet<>())")
    UserEntity mapFromUserToEntity(User user);


}


