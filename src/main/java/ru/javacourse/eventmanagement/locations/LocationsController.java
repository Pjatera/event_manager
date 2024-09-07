package ru.javacourse.eventmanagement.locations;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javacourse.eventmanagement.locations.model.EventLocationDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationsController {

    @GetMapping
    public ResponseEntity<List<EventLocationDto>> getLocations() {
        List<EventLocationDto> dtoResponse = List.of(new EventLocationDto(42,"Ледовый дворец", "г. СПб, Пятилеток 1",12300,"Спортивно-концертный комплекс в Санкт-Петербурге"));
        return ResponseEntity.status(HttpStatus.OK).body(dtoResponse);
    }
}
