package ru.javacourse.eventmanagement.web.dto.event;

import jakarta.validation.constraints.*;
import ru.javacourse.eventmanagement.domain.event.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventDto(
        @NotNull(message = "ID not be null")
        Long id,

        @NotBlank(message = "Name must not be null or empty")
        String name,

        @NotNull(message = "Owner ID not be null")
        Long ownerId,

        @Positive(message = "maxPlaces must be greater than zero")
        int maxPlaces,

        @PositiveOrZero(message = "occupied places must not be negative")
        int occupiedPlaces,

        @Future(message = "Date must be in future")
        LocalDateTime date,

        @PositiveOrZero(message = "Cost must not be negative")
        BigDecimal cost,

        @Min(value = 30, message = "Duration must be at least 30 minutes")
        int duration,

        @NotNull(message = "locationId not be null")
        Long locationId,

        @NotNull(message = "EventStatus not be null")
        EventStatus status

) {
}
