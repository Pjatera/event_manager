package ru.javacourse.eventmanagement.locations.model;

public record EventLocationDto(
        Integer id,
        String name,
        String address,
        int capacity,
        String description) {
}
