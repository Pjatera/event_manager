package ru.javacourse.eventmanagement.locations;

public record LocationDto(
        Integer id,
        String name,
        String address,
        int capacity,
        String description) {
}
