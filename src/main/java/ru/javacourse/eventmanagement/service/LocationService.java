package ru.javacourse.eventmanagement.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.javacourse.eventmanagement.domain.exeptions.NotFoundLocation;
import ru.javacourse.eventmanagement.domain.locations.Location;
import ru.javacourse.eventmanagement.domain.locations.LocationMapper;
import ru.javacourse.eventmanagement.db.entity.location.LocationEntity;
import ru.javacourse.eventmanagement.db.repository.LocationRepository;

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

    @Transactional
    public Location createNewLocation(@Valid @NotNull Location location) {
        var locationEntity = locationMapper.mapToEntity(location);
        locationEntity = locationRepository.save(locationEntity);
        return locationMapper.mapFromEntity(locationEntity);
    }

    @Transactional
    public Location deleteLocation(@PositiveOrZero(message = "ID must be positive or zero") @NotNull Long locationId) {
        var locationEntity = getLocationEntityById(locationId);
        locationRepository.deleteById(locationId);
        return locationMapper.mapFromEntity(locationEntity);
    }

    public Location getLocationByID(@PositiveOrZero(message = "ID must be positive or zero") @NotNull Long locationId) {
        return locationMapper.mapFromEntity(getLocationEntityById(locationId));
    }

    private LocationEntity getLocationEntityById(@NotNull Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundLocation("Location with ID %d not found".formatted(locationId)));
    }

    @Transactional
    public Location updateById(@PositiveOrZero(message = "ID must be positive or zero") @NotNull Long locationId, @Valid @NotNull Location location) {
        var locationEntity = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundLocation("Location with ID %d not found".formatted(locationId)));
        isChangingCapacityCorrect(location, locationEntity);
        locationEntity.setName(location.name());
        locationEntity.setAddress(location.address());
        locationEntity.setCapacity(location.capacity());
        locationEntity.setDescription(location.description());
        var updateLocation = locationRepository.save(locationEntity);
        return locationMapper.mapFromEntity(updateLocation);
    }

    private void isChangingCapacityCorrect(@NotNull Location locationUpdate, @NotNull LocationEntity locationByIdOld) {
        if (locationByIdOld.getCapacity() > locationUpdate.capacity()) {
            throw new IllegalArgumentException("The location with the ID %d is too small, it cannot be less than %d".formatted(locationByIdOld.getId(), locationByIdOld.getCapacity()));
        }
    }
}
