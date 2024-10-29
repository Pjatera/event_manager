package ru.javacourse.eventmanagement.web.dto.locations;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import org.hibernate.validator.constraints.Length;

public record LocationDto(
        @Null(message = "For a new EventLocation, the ID must be empty")
        Long id,
        @NotBlank(message = "name not be empty")
        @Length(max = 255, message = "Name length must be smaller than 255 symbols")
        String name,
        @NotBlank(message = "address not be empty")
        @Length(max = 255, message = "Address length must be smaller than 255 symbols")
        String address,
        @Min(value = 5, message = "the capacity must be greater than 5")
        int capacity,
        @Length(max = 255, message = "Description length must be smaller than 255 symbols")
        String description) {
}
