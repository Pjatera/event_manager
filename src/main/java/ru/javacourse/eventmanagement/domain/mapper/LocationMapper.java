package ru.javacourse.eventmanagement.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.javacourse.eventmanagement.db.entity.location.LocationEntity;
import ru.javacourse.eventmanagement.domain.locations.Location;
import ru.javacourse.eventmanagement.web.dto.locations.LocationDto;

import java.util.HashSet;


@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {HashSet.class})
public interface LocationMapper {
    LocationDto mapLocationToDto(Location location);


    Location mapFromDtoToLocation(LocationDto locationDto);

    Location mapFromEntityToLocation(LocationEntity locationEntity);

    @Mapping(target = "events", expression = "java(new HashSet<>())")
    LocationEntity mapFromLocationToEntity(Location location);

}
