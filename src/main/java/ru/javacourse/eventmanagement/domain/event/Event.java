package ru.javacourse.eventmanagement.domain.event;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record Event(
        Long id,
        String name,
        Long ownerId,
        int maxPlaces,
        int occupiedPlaces,
        LocalDateTime date,
        int cost,
        int duration,
        Long locationId,
        EventStatus status
) {
}
