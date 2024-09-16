package ru.javacourse.eventmanagement.domain.exeptions;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundLocation extends EntityNotFoundException {
    public NotFoundLocation(String message) {
        super(message);
    }
}
