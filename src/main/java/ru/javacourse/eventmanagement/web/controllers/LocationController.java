package ru.javacourse.eventmanagement.web.controllers;


import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javacourse.eventmanagement.domain.locations.Location;
import ru.javacourse.eventmanagement.domain.mapper.LocationMapper;
import ru.javacourse.eventmanagement.service.LocationService;
import ru.javacourse.eventmanagement.web.dto.locations.LocationDto;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private static final String TEMPLATE_MESSAGE_ID_VALIDATION = "Id must be positive or equal to 0";
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @GetMapping
    public ResponseEntity<List<LocationDto>> getLocations() {
        log.info("Get all locations");
        var locationDtoList = locationService.findAll().stream().map(locationMapper::mapLocationToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(locationDtoList);
    }


    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody @Valid LocationDto locationDto) {
        var newLocation = locationService.createNewLocation(locationMapper.mapFromDtoToLocation(locationDto));
        log.info("Create a new location{}", newLocation);
        return ResponseEntity.status(HttpStatus.CREATED).body(locationMapper.mapLocationToDto(newLocation));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteLocation(@PathVariable @PositiveOrZero(message = TEMPLATE_MESSAGE_ID_VALIDATION)  Long locationId) {
        var location = locationService.deleteLocation(locationId);
        log.info("Delete a location {}", location);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(locationMapper.mapLocationToDto(location));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable @PositiveOrZero(message = TEMPLATE_MESSAGE_ID_VALIDATION) Long locationId) {
        var locationByID = locationService.getLocationByID(locationId);
        log.info("Get a location {}", locationByID);
        return ResponseEntity.status(HttpStatus.OK).body(locationMapper.mapLocationToDto(locationByID));
    }


    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocationByID(@PathVariable @PositiveOrZero(message = TEMPLATE_MESSAGE_ID_VALIDATION) Long locationId
            , @RequestBody @Valid LocationDto locationDto) {
        var eventLocation = locationMapper.mapFromDtoToLocation(locationDto);
        Location locationUpdate = locationService.updateById(locationId, eventLocation);
        log.info("Update a location {}", locationUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(locationMapper.mapLocationToDto(locationUpdate));
    }

}

