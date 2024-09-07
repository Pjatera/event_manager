package ru.javacourse.eventmanagement.locations.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import ru.javacourse.eventmanagement.utill.Marker;


public record EventLocation(
        @NotNull(groups = Marker.OnUpdate.class ,message = "Id cannot be null")
        @Null(groups = Marker.OnCreate.class,message = "For a new EventLocation, the ID must be empty")
        Integer id,
        @NotBlank(message = "name not be empty")
        String name,
        @NotBlank(message = "address not be empty")
        String address,
        @NotNull(message = "capacity not be empty!")
        @Min(value = 5, message = "the capacity must be greater than 5")
        int capacity,
        String description) {
}
