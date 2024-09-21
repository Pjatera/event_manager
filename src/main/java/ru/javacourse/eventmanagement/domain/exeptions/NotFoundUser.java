package ru.javacourse.eventmanagement.domain.exeptions;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundUser extends EntityNotFoundException {
    public NotFoundUser(String message) {
        super(message);
    }
}
