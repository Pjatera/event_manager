package ru.javacourse.eventmanagement.locations;


import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javacourse.eventmanagement.locations.model.EventLocation;
import ru.javacourse.eventmanagement.locations.model.EventLocationDto;
import ru.javacourse.eventmanagement.locations.model.EventLocationMapper;
import ru.javacourse.eventmanagement.utill.Marker;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final EventLocationMapper eventLocationMapper;

    @GetMapping
    public ResponseEntity<List<EventLocationDto>> getLocations() {
        var eventLocationDtoList = locationService.findAll().stream().map(eventLocationMapper::mapToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(eventLocationDtoList);
    }


    @PostMapping
    public ResponseEntity<EventLocationDto> createLocation(@RequestBody @Valid EventLocationDto eventLocationDto) {
        var newLocation = locationService.createNewLocation(eventLocationMapper.mapFromDto(eventLocationDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(eventLocationMapper.mapToDto(newLocation));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<EventLocationDto> deleteLocation(@PathVariable @PositiveOrZero(message = "ID must be positive or zero") Integer locationId) {
        var eventLocation = locationService.deleteLocation(locationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(eventLocationMapper.mapToDto(eventLocation));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<EventLocationDto> getLocationById(@PathVariable @PositiveOrZero(message = "ID must be positive or zero") Integer locationId) {
        var locationByID = locationService.getLocationByID(locationId);
        return ResponseEntity.status(HttpStatus.OK).body(eventLocationMapper.mapToDto(locationByID));
    }


    @PutMapping("/{locationId}")
    public ResponseEntity<EventLocationDto> updateLocationByID(@PathVariable @PositiveOrZero(message = "ID must be positive or zero") Integer locationId
            ,@RequestBody @Valid EventLocationDto eventLocationDto) {
        var eventLocation = eventLocationMapper.mapFromDto(eventLocationDto);
      EventLocation eventLocationUpdate = locationService.updateById(locationId,eventLocation);
       return ResponseEntity.status(HttpStatus.OK).body(eventLocationMapper.mapToDto(eventLocationUpdate));


    }
}
