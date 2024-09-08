package ru.javacourse.eventmanagement.locations.model;

import org.springframework.stereotype.Component;

@Component
public class EventLocationMapper {

    public EventLocationDto mapToDto(EventLocation eventLocation) {
        return new EventLocationDto(
                eventLocation.id(),
                eventLocation.name(),
                eventLocation.address(),
                eventLocation.capacity(),
                eventLocation.description()
        );
    }

    public EventLocation mapFromDto(EventLocationDto eventLocationDto) {
        return new EventLocation(
                eventLocationDto.id(),
                eventLocationDto.name(),
                eventLocationDto.address(),
                eventLocationDto.capacity(),
                eventLocationDto.description()
        );
    }

    public EventLocation mapFromEntity(EventLocationEntity eventLocationEntity) {
        return new EventLocation(
                eventLocationEntity.getId(),
                eventLocationEntity.getName(),
                eventLocationEntity.getAddress(),
                eventLocationEntity.getCapacity(),
                eventLocationEntity.getDescription()
        );
    }

    public EventLocationEntity mapToEntity(EventLocation eventLocation) {
        return new EventLocationEntity(
                eventLocation.id(),
                eventLocation.name(),
                eventLocation.address(),
                eventLocation.capacity(),
                eventLocation.description()
        );
    }
}
