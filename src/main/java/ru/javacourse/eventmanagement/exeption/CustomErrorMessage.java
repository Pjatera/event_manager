package ru.javacourse.eventmanagement.exeption;

import java.time.LocalDateTime;

public record CustomErrorMessage(String message,String detailedMessage ,LocalDateTime dateTime) {
}
