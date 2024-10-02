package ru.javacourse.eventmanagement.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record Event(
        Long id,
        String name,
        Long ownerId,
        int maxPlaces,
        LocalDateTime date,
        BigDecimal cost,
        int duration,
        Long locationId,
        EventStatus status,
        Set<Registration> registrations
) {
}
