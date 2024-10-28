package ru.javacourse.eventmanagement.domain.exeptions;

import java.time.LocalDateTime;

public record ErrorMessageResponse(String message, String detailedMessage, LocalDateTime dateTime) {
}
