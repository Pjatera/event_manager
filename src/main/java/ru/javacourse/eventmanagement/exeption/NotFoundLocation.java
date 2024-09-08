package ru.javacourse.eventmanagement.exeption;

public class NotFoundLocation extends RuntimeException {
    public NotFoundLocation(String message) {
        super(message);
    }
}
