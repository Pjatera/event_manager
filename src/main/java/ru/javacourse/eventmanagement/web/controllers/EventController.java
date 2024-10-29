package ru.javacourse.eventmanagement.web.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javacourse.eventmanagement.domain.event.Event;
import ru.javacourse.eventmanagement.domain.mapper.EventMapper;
import ru.javacourse.eventmanagement.domain.service.EventRegistrationService;
import ru.javacourse.eventmanagement.domain.service.EventService;
import ru.javacourse.eventmanagement.web.dto.event.EventCreateRequestDto;
import ru.javacourse.eventmanagement.web.dto.event.EventDto;
import ru.javacourse.eventmanagement.web.dto.event.EventSearchRequestDto;
import ru.javacourse.eventmanagement.web.dto.event.EventUpdateRequestDto;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {
    private static final String MESSAGE_TEMPLATE_FOR_CORRECTNESS_ID = "Id must be positive or equal to 0";
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final EventRegistrationService eventRegistrationService;

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody
                                                @Valid
                                                EventCreateRequestDto eventCreateRequestDto
    ) {
        log.info("Creating event: {}", eventCreateRequestDto);
        var event = eventService.createEvent(eventCreateRequestDto);
        var eventDto = eventMapper.mapFromEventToDto(event);
        log.info("Created event: {}", eventDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventDto);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEventById(@PathVariable
                                                @PositiveOrZero(message = MESSAGE_TEMPLATE_FOR_CORRECTNESS_ID)
                                                Long eventId
    ) {
        log.info("Deleting event: {}", eventId);
        eventService.deleteEvent(eventId);
        log.info("Deleted event: {}", eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable
                                                 @PositiveOrZero(message = MESSAGE_TEMPLATE_FOR_CORRECTNESS_ID)
                                                 Long eventId) {
        log.info("Getting event with ID: {}", eventId);
        Event event = eventService.getEventById(eventId);
        var eventDto = eventMapper.mapFromEventToDto(event);
        log.info("Got event: {}", eventDto);
        return ResponseEntity.status(HttpStatus.OK).body(eventDto);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEventByID(@PathVariable
                                                    @PositiveOrZero(message = MESSAGE_TEMPLATE_FOR_CORRECTNESS_ID)
                                                    Long eventId,
                                                    @RequestBody
                                                    @Valid
                                                    EventUpdateRequestDto eventDtoUpdate
    ) {
        log.info("Updating event with ID: {}", eventId);
        var event = eventService.updateEventById(eventDtoUpdate, eventId);
        var eventDto = eventMapper.mapFromEventToDto(event);
        log.info("Updated event: {}", eventDto);
        return ResponseEntity.status(HttpStatus.OK).body(eventDto);

    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyEvents() {
        log.info("Getting events created by the user");
        var allUsersEvents = eventService.getAllUsersEvents();
        log.info("Got {} events", allUsersEvents.size());
        return ResponseEntity.status(HttpStatus.OK).body(eventMapper.mapFromEventListToDtoList(allUsersEvents));
    }

    @PostMapping("registrations/{eventId}")
    public ResponseEntity<Void> userRegistrationForAnEvent(@PathVariable
                                                           @PositiveOrZero(message = MESSAGE_TEMPLATE_FOR_CORRECTNESS_ID)
                                                           Long eventId) {
        log.info("Registering user for an event: {}", eventId);
        eventRegistrationService.registrationForAnEvent(eventId);
        log.info("Registered user for an event: {}", eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/registrations/cancel/{eventId}")
    public ResponseEntity<Void> cancelUserRegistration(@PathVariable
                                                       @PositiveOrZero(message = MESSAGE_TEMPLATE_FOR_CORRECTNESS_ID)
                                                       Long eventId) {
        log.info("Canceling user for an event: {}", eventId);
        eventRegistrationService.cancelUserRegistration(eventId);
        log.info("Cancelled user for an event: {}", eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/registrations/my")
    public ResponseEntity<List<EventDto>> getMyRegistrations() {
        log.info("Getting events for which the user has registered");
        var myRegistrations = eventService.getMyRegistrations();
        log.info("Got {} events for which the user has registered", myRegistrations.size());
        return ResponseEntity.status(HttpStatus.OK).body(eventMapper.mapFromEventListToDtoList(myRegistrations));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> getEventsByFilters(@RequestBody EventSearchRequestDto eventSearchRequestDto) {
        log.info("Getting events by filter {}", eventSearchRequestDto);
        var eventsByFilters = eventService.findEventsByFilters(eventSearchRequestDto);
        log.info("Got {} events by filter {}", eventsByFilters.size(), eventSearchRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(eventMapper.mapFromEventListToDtoList(eventsByFilters));
    }


}
