package ru.javacourse.eventmanagement.domain.event;


import ru.javacourse.eventmanagement.db.entity.event.EventEntity;
import ru.javacourse.eventmanagement.db.entity.user.UserEntity;

public record Registration(
        Long id,
        EventEntity event,
        UserEntity user
) {
}
