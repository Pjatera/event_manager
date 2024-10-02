package ru.javacourse.eventmanagement.web.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventUpdateRequestDto(

        @NotBlank(message = "name not be empty")
        @Length(max = 255, message = "Name length must be smaller than 255 symbols")
        String name,
        @Positive(message = "maxPlaces must be positive ")
        Integer maxPlaces,
        @Future(message = "Date must be in future")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime date,
        @PositiveOrZero(message = "Cost must not be negative")
        BigDecimal cost,
        @Min(value = 30, message = "Duration must be greater than 30")
        Integer duration,
        @PositiveOrZero(message = "locationId must not be negative")
        Long locationId

) {
}
