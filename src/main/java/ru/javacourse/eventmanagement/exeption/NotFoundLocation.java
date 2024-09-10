package ru.javacourse.eventmanagement.exeption;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundLocation extends EntityNotFoundException {
    public NotFoundLocation(String message) {
        super(message);
    }
}
