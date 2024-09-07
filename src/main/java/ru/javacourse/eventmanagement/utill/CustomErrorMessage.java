package ru.javacourse.eventmanagement.utill;

import java.time.LocalDateTime;

public record CustomErrorMessage(String message,String detailedMessage ,LocalDateTime dateTime) {
}
