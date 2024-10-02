package ru.javacourse.eventmanagement.web.dto.event;

import ru.javacourse.eventmanagement.domain.event.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchRequestDto(
        String name,
        Integer placesMin,
        Integer placesMax,
        LocalDateTime dateStartAfter,
        LocalDateTime dateStartBefore,
        BigDecimal costMin,
        BigDecimal costMax,
        Integer durationMin,
        Integer durationMax,
        Integer locationId,
        EventStatus eventStatus
) {
}
