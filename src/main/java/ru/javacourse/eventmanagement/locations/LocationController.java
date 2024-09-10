package ru.javacourse.eventmanagement.locations;


import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javacourse.eventmanagement.locations.model.Location;
import ru.javacourse.eventmanagement.locations.model.LocationDto;
import ru.javacourse.eventmanagement.locations.model.LocationMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @GetMapping
    public ResponseEntity<List<LocationDto>> getLocations() {
        log.info("Get all locations");
        var locationDtoList = locationService.findAll().stream().map(locationMapper::mapToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(locationDtoList);
    }


    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody  LocationDto locationDto) {
        var newLocation = locationService.createNewLocation(locationMapper.mapFromDto(locationDto));
        log.info("Create a new location{}", newLocation);
        return ResponseEntity.status(HttpStatus.CREATED).body(locationMapper.mapToDto(newLocation));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteLocation(@PathVariable Integer locationId) {
        var location = locationService.deleteLocation(locationId);
        log.info("Delete a location {}", location);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(locationMapper.mapToDto(location));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable Integer locationId) {
        var locationByID = locationService.getLocationByID(locationId);
        log.info("Get a location {}", locationByID);
        return ResponseEntity.status(HttpStatus.OK).body(locationMapper.mapToDto(locationByID));
    }


    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocationByID(@PathVariable  Integer locationId
            , @RequestBody LocationDto locationDto) {
        var eventLocation = locationMapper.mapFromDto(locationDto);
        Location locationUpdate = locationService.updateById(locationId,eventLocation);
        log.info("Update a location {}", locationUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(locationMapper.mapToDto(locationUpdate));


    }
}
