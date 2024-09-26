package ru.javacourse.eventmanagement.domain.exeptions;

import java.time.LocalDateTime;

public record CustomErrorMessage(String message, String detailedMessage, LocalDateTime dateTime) {
}
