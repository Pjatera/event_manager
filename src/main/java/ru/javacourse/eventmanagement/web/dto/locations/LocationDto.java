package ru.javacourse.eventmanagement.web.dto.locations;

public record LocationDto(
        Long id,
        String name,
        String address,
        int capacity,
        String description) {
}
