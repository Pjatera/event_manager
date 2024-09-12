package ru.javacourse.eventmanagement.exeptions;

import java.time.LocalDateTime;

public record CustomErrorMessage(String message,String detailedMessage ,LocalDateTime dateTime) {
}
