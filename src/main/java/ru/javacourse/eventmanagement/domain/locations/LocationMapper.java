package ru.javacourse.eventmanagement.domain.locations;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.javacourse.eventmanagement.db.entity.location.LocationEntity;
import ru.javacourse.eventmanagement.web.dto.locations.LocationDto;


@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {
    LocationDto mapToDto(Location location);

    Location mapFromDto(LocationDto locationDto);

    Location mapFromEntity(LocationEntity locationEntity);

    LocationEntity mapToEntity(Location location);
}
