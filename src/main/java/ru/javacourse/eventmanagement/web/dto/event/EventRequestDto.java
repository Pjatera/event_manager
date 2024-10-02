package ru.javacourse.eventmanagement.web.dto.event;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import ru.javacourse.eventmanagement.domain.validation.OnCreate;
import ru.javacourse.eventmanagement.domain.validation.OnUpdate;

import java.time.LocalDateTime;

public record EventRequestDto(
        @NotBlank(groups = OnCreate.class,message = "name not be empty")
        @Length(groups = OnCreate.class,max = 255, message = "Name length must be smaller than 255 symbols")
        String name,
        @Positive(groups = {OnCreate.class, OnUpdate.class},message = "maxPlaces must be positive ")
        int maxPlaces,
        @Future(groups = {OnCreate.class, OnUpdate.class},message = "Date must be in future")
        LocalDateTime date,
        @PositiveOrZero(groups = {OnCreate.class, OnUpdate.class},message = "Cost must not be negative")
        int cost,
        @Min(groups = {OnCreate.class, OnUpdate.class},value = 30, message = "Duration must be greater than 30")
        int duration,
        @NotNull(groups = OnCreate.class,message = "locationId not be null")
        Long locationId

) {
}
