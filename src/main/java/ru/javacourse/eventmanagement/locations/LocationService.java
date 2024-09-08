package ru.javacourse.eventmanagement.locations;


import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.javacourse.eventmanagement.locations.model.EventLocation;
import ru.javacourse.eventmanagement.locations.model.EventLocationEntity;
import ru.javacourse.eventmanagement.locations.model.EventLocationMapper;
import ru.javacourse.eventmanagement.utill.Marker;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class LocationService {

    private final LocationRepository locationRepository;
    private final EventLocationMapper eventLocationMapper;

    public List<EventLocation> findAll() {
        return locationRepository.findAll()
                .stream()
                .map(eventLocationMapper::mapFromEntity)
                .toList();
    }

    @Validated({Marker.OnCreate.class})
    public EventLocation createNewLocation(@Valid EventLocation location) {
        var eventLocationEntity = eventLocationMapper.mapToEntity(location);
        eventLocationEntity = locationRepository.save(eventLocationEntity);
        return eventLocationMapper.mapFromEntity(eventLocationEntity);
    }


    public EventLocation deleteLocation(@PositiveOrZero(message = "ID must be positive or zero") Integer locationId) {
        var eventLocationEntity = getEventLocationEntityById(locationId);
        locationRepository.deleteById(locationId);
        return eventLocationMapper.mapFromEntity(eventLocationEntity);
    }

    public EventLocation getLocationByID(@PositiveOrZero(message = "ID must be positive or zero") Integer locationId) {
        return eventLocationMapper.mapFromEntity(getEventLocationEntityById(locationId));
    }

    private EventLocationEntity getEventLocationEntityById(Integer locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location with ID %d not found".formatted(locationId)));
    }


    public EventLocation updateById(@PositiveOrZero(message = "ID must be positive or zero") Integer locationId, EventLocation eventLocation) {
        var locationByIdOld = getLocationByID(locationId);
        isChangingCapacityCorrect(eventLocation, locationByIdOld);
        locationRepository.save(eventLocationMapper.mapToEntity(eventLocation));
        return locationByIdOld;
    }

    private static void isChangingCapacityCorrect(EventLocation eventLocationUpdate, EventLocation locationByIdOld) {
        if (locationByIdOld.capacity() < eventLocationUpdate.capacity()) {
            throw new IllegalArgumentException("The location with the ID %d is too small, it cannot be less than %d".formatted(locationByIdOld.id(), locationByIdOld.capacity()));
        }
    }
}
