package ru.javacourse.eventmanagement.locations;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.javacourse.eventmanagement.locations.model.Location;
import ru.javacourse.eventmanagement.locations.model.LocationEntity;
import ru.javacourse.eventmanagement.locations.model.LocationMapper;
import ru.javacourse.eventmanagement.utill.Marker;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Validated
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public List<Location> findAll() {
        return locationRepository.findAll()
                .stream()
                .map(locationMapper::mapFromEntity)
                .toList();
    }

    @Validated({Marker.OnCreate.class})
    @Transactional
    public Location createNewLocation(@Valid Location location) {
        var locationEntity = locationMapper.mapToEntity(location);
        locationEntity = locationRepository.save(locationEntity);
        return locationMapper.mapFromEntity(locationEntity);
    }

    @Transactional
    public Location deleteLocation(@PositiveOrZero(message = "ID must be positive or zero") Integer locationId) {
        var locationEntity = getLocationEntityById(locationId);
        locationRepository.deleteById(locationId);
        return locationMapper.mapFromEntity(locationEntity);
    }

    public Location getLocationByID(@PositiveOrZero(message = "ID must be positive or zero") Integer locationId) {
        return locationMapper.mapFromEntity(getLocationEntityById(locationId));
    }

    private LocationEntity getLocationEntityById(Integer locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location with ID %d not found".formatted(locationId)));
    }

    @Transactional
    public Location updateById(@PositiveOrZero(message = "ID must be positive or zero") Integer locationId, @Valid Location location) {
        var locationByIdOld = getLocationByID(locationId);
        isChangingCapacityCorrect(location, locationByIdOld);
        locationRepository.save(locationMapper.mapToEntity(location));
        return locationByIdOld;
    }

    private  void isChangingCapacityCorrect(Location locationUpdate, Location locationByIdOld) {
        if (locationByIdOld.capacity() < locationUpdate.capacity()) {
            throw new IllegalArgumentException("The location with the ID %d is too small, it cannot be less than %d".formatted(locationByIdOld.id(), locationByIdOld.capacity()));
        }
    }
}
