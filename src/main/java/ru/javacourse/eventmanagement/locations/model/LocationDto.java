package ru.javacourse.eventmanagement.locations.model;

public record LocationDto(
        Integer id,
        String name,
        String address,
        int capacity,
        String description) {
}
