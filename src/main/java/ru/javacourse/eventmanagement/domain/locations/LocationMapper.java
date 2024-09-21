package ru.javacourse.eventmanagement.domain.locations;

import org.mapstruct.Mapper;
import ru.javacourse.eventmanagement.entity.location.LocationEntity;
import ru.javacourse.eventmanagement.web.dto.locations.LocationDto;


@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDto mapToDto(Location location);

    Location mapFromDto(LocationDto locationDto);

    Location mapFromEntity(LocationEntity locationEntity);

    LocationEntity mapToEntity(Location location);
}
