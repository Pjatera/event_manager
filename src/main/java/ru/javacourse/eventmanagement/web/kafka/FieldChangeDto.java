package ru.javacourse.eventmanagement.web.kafka;

public record FieldChangeDto<T>(T oldField, T newField) {


}
