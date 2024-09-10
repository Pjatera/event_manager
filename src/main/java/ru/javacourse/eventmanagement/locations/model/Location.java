package ru.javacourse.eventmanagement.locations.model;

import jakarta.validation.constraints.*;
import ru.javacourse.eventmanagement.utill.Marker;

public record Location(

        @Null(groups = Marker.OnCreate.class,message = "For a new EventLocation, the ID must be empty")
        Integer id,
        @NotBlank(groups = Marker.OnCreate.class,message = "name not be empty")
        String name,
        @NotBlank(message = "address not be empty")
        String address,
        @NotNull(message = "capacity not be empty!")
        @Min(value = 5, message = "the capacity must be greater than 5")
        int capacity,
        String description) {
}
