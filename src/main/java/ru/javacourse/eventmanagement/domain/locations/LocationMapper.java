package ru.javacourse.eventmanagement.domain.locations;

import org.springframework.stereotype.Component;
import ru.javacourse.eventmanagement.entity.location.LocationEntity;
import ru.javacourse.eventmanagement.domain.Mapper;
import ru.javacourse.eventmanagement.web.dto.locations.LocationDto;

@Component
public class LocationMapper implements Mapper<LocationDto, Location, LocationEntity> {
    @Override
    public LocationDto mapToDto(Location location) {
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    @Override
    public Location mapFromDto(LocationDto locationDto) {
        return new Location(
                locationDto.id(),
                locationDto.name(),
                locationDto.address(),
                locationDto.capacity(),
                locationDto.description()
        );
    }

    @Override
    public Location mapFromEntity(LocationEntity locationEntity) {
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription()
        );
    }

    @Override
    public LocationEntity mapToEntity(Location location) {
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }
}