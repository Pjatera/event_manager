package ru.javacourse.eventmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javacourse.eventmanagement.db.entity.event.EventEntity;
import ru.javacourse.eventmanagement.db.repository.EventRepository;
import ru.javacourse.eventmanagement.domain.event.Event;
import ru.javacourse.eventmanagement.domain.event.EventStatus;
import ru.javacourse.eventmanagement.domain.locations.Location;
import ru.javacourse.eventmanagement.domain.mapper.EventMapper;
import ru.javacourse.eventmanagement.domain.mapper.LocationMapper;
import ru.javacourse.eventmanagement.domain.mapper.UserMapper;
import ru.javacourse.eventmanagement.service.security.AuthenticationService;
import ru.javacourse.eventmanagement.web.dto.event.EventCreateRequestDto;
import ru.javacourse.eventmanagement.web.dto.event.EventSearchRequestDto;
import ru.javacourse.eventmanagement.web.dto.event.EventUpdateRequestDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;
    private final LocationService locationService;
    private final LocationMapper locationMapper;
    private final AuthenticationService authenticationService;


    @Transactional
    public Event createEvent(EventCreateRequestDto eventCreateRequestDto) {
        var event = eventMapper.mapFromRequestDtoToEvent(eventCreateRequestDto);
        var user = authenticationService.getCurrentUserAuthenticated().getUser();
        var location = locationService.getLocationByID(event.locationId());
        var locationEntity = locationMapper.mapFromLocationToEntity(location);
        if (eventRepository.hasEventInTimeFrame(eventCreateRequestDto.date(), eventCreateRequestDto.duration())) {
            throw new IllegalArgumentException("It is impossible to hold the event because the time is busy");
        }
        var userEntity = userMapper.mapFromUserToEntity(user);
        var eventEntity = eventMapper.mapFromEventToEntity(event);
        eventEntity.setLocation(locationEntity);
        eventEntity.setOwnerUser(userEntity);
        if (eventEntity.getLocation().getCapacity() < event.maxPlaces()) {
            log.error("The capacity of the event is greater than the capacity of the location with ID:{} ", event.locationId());
            throw new IllegalArgumentException("Error creating event, capacity of the location does not allow the event to be held ");
        }
        eventRepository.save(eventEntity);
        return eventMapper.mapFromEntityToEvent(eventEntity);
    }

    @Transactional
    public void deleteEvent(@PositiveOrZero(message = "Id must be positive or equal to 0")
                            Long eventId
    ) {
        var eventEntity = getEventEntity(eventId);
        if (isRoleAdmin() || isThisUserWhoCreatedTheEvent(eventEntity)) {
            if (eventEntity.getStatus() != EventStatus.WAIT_START) {
                throw new IllegalArgumentException("The event cannot be canceled");
            }
            eventEntity.setStatus(EventStatus.CANCELLED);
        } else {
            throw new IllegalArgumentException("User is not allowed to delete this event");
        }

    }

    private EventEntity getEventEntity(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("event not fount"));
    }


    @Transactional
    public Event updateEventById(EventUpdateRequestDto eventDtoUpdate, Long eventId) {
        var eventEntity = getEventEntity(eventId);
        var locationId = Optional.ofNullable(eventDtoUpdate.locationId()).orElse(eventEntity.getLocation().getId());
        if (isRoleAdmin() || isThisUserWhoCreatedTheEvent(eventEntity)) {
            var location = locationService.getLocationByID(locationId);
            if (isNotValidUpdate(eventEntity, eventDtoUpdate, location)) {
                throw new BadCredentialsException("MaxPlaces invalid update parameters");
            }
            var duration = Optional.ofNullable(eventDtoUpdate.duration()).orElse(eventEntity.getDuration());
            if (eventRepository.hasEventInTimeFrame(eventDtoUpdate.date(), duration)) {
                throw new IllegalArgumentException("It is impossible to hold the event because the time is busy");
            }
            Optional.ofNullable(eventDtoUpdate.name()).ifPresent(eventEntity::setName);
            Optional.ofNullable(eventDtoUpdate.date()).ifPresent(eventEntity::setDate);
            eventEntity.setDuration(duration);
            Optional.ofNullable(eventDtoUpdate.cost()).ifPresent(eventEntity::setCost);
            Optional.ofNullable(eventDtoUpdate.maxPlaces()).ifPresent(eventEntity::setMaxPlaces);
            eventEntity.setLocation(locationMapper.mapFromLocationToEntity(location));
            eventRepository.save(eventEntity);
        } else {
            var user = authenticationService.getCurrentUserAuthenticated().getUser();
            log.error("User with login {} is not the event creator", user.login());
            throw new BadCredentialsException("User with login %s is not the event creator".formatted(user.login()));
        }
        return eventMapper.mapFromEntityToEvent(eventEntity);
    }

    @Transactional
    public Event getEventById(@PositiveOrZero(message = "Id must be positive or equal to 0") Long eventId) {
        var eventEntity = getEventEntity(eventId);
        return eventMapper.mapFromEntityToEvent(eventEntity);
    }

    public List<Event> getAllUsersEvents() {
        var user = authenticationService.getCurrentUserAuthenticated().getUser();
        var eventEntityList = eventRepository.findEventsByOwnerUserLogin(user.login());
        return eventMapper.mapFromEntityListToEventList(eventEntityList);
    }

    private boolean isThisUserWhoCreatedTheEvent(EventEntity eventEntity) {
        var user = authenticationService.getCurrentUserAuthenticated().getUser();
        return user.id().equals(eventEntity.getOwnerUser().getId());
    }

    private boolean isRoleAdmin() {
        return authenticationService.getAuthoritiesCurrentUserAuthenticated()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean isNotValidUpdate(EventEntity eventEntity, EventUpdateRequestDto eventDtoUpdate, Location location) {
        var occupiedPlaces = eventEntity.getEventsRegistrationEntities().size();
        int maxPlacesUpdate = Optional.ofNullable(eventDtoUpdate.maxPlaces()).orElse(eventEntity.getMaxPlaces());
        var capacity = location.capacity();
        return maxPlacesUpdate < occupiedPlaces || maxPlacesUpdate > capacity;
    }

    public List<Event> findEventsByFilters(EventSearchRequestDto eventSearchRequestDto) {
        var locationId = eventSearchRequestDto.locationId();
        Location location = locationId == null ? null : locationService.getLocationByID(locationId);
        var events = eventRepository.findByFilters(eventSearchRequestDto.name(),
                eventSearchRequestDto.placesMin(),
                eventSearchRequestDto.placesMax(),
                eventSearchRequestDto.dateStartAfter(),
                eventSearchRequestDto.dateStartBefore(),
                eventSearchRequestDto.costMin(),
                eventSearchRequestDto.costMax(),
                eventSearchRequestDto.durationMin(),
                eventSearchRequestDto.durationMax(),
                locationMapper.mapFromLocationToEntity(location),
                eventSearchRequestDto.eventStatus()
        );
        return eventMapper.mapFromEntityListToEventList(events);
    }

    @Transactional
    public void performTaskWithUpdateEventStatus() {
        log.info("Checking event status");

        var countEventsChangedToStartedStatus = eventRepository.checkingAndUpdatingTheStatusOfStartedEvents(EventStatus.WAIT_START, EventStatus.STARTED);
        log.info("{} events have their status changed to {}", countEventsChangedToStartedStatus, EventStatus.STARTED);
        var countEventsChangedToFinishedStatus = eventRepository.checkingAndUpdatingTheStatusOfFinishedEvents(EventStatus.STARTED.toString(), EventStatus.FINISHED.toString());
        log.info("{} events have their status changed to {}", countEventsChangedToFinishedStatus, EventStatus.FINISHED);
    }

    public List<Event> getMyRegistrations() {
        var user = authenticationService.getCurrentUserAuthenticated().getUser();
        var byUser = eventRepository.findRegistrationEventsByUser(user.login());
        return eventMapper.mapFromEntityListToEventList(byUser);
    }
}
