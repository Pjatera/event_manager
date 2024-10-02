package ru.javacourse.eventmanagement.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.javacourse.eventmanagement.db.entity.event.EventEntity;
import ru.javacourse.eventmanagement.db.entity.event.RegistrationEntity;
import ru.javacourse.eventmanagement.domain.event.Event;
import ru.javacourse.eventmanagement.domain.event.Registration;
import ru.javacourse.eventmanagement.web.dto.event.EventCreateRequestDto;
import ru.javacourse.eventmanagement.web.dto.event.EventDto;
import ru.javacourse.eventmanagement.web.dto.event.EventUpdateRequestDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {HashSet.class})
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "WAIT_START")
    @Mapping(target = "registrations", expression = "java(new HashSet<>())")
    Event mapFromRequestDtoToEvent(EventCreateRequestDto eventCreateRequestDto);


    @Mapping(target = "occupiedPlaces", expression = "java(event.registrations().size())")
    EventDto mapFromEventToDto(Event event);


    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "ownerId", source = "ownerUser.id")
    @Mapping(target = "registrations", source = "eventsRegistrationEntities")
    Event mapFromEntityToEvent(EventEntity eventEntity);


    @Mapping(target = "id", source = "event.id")
    @Mapping(target = "name", source = "event.name")
    @Mapping(target = "eventsRegistrationEntities", expression = "java(new HashSet<>())")
    EventEntity mapFromEventToEntity(Event event);


    List<EventDto> mapFromEventListToDtoList(List<Event> eventList);

    List<Event> mapFromEntityListToEventList(List<EventEntity> eventEntityList);


}
