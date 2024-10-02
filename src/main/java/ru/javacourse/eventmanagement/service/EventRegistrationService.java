package ru.javacourse.eventmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javacourse.eventmanagement.db.entity.event.RegistrationEntity;
import ru.javacourse.eventmanagement.db.repository.RegistrationRepository;
import ru.javacourse.eventmanagement.domain.event.Event;
import ru.javacourse.eventmanagement.domain.event.EventStatus;
import ru.javacourse.eventmanagement.domain.mapper.EventMapper;
import ru.javacourse.eventmanagement.domain.mapper.UserMapper;
import ru.javacourse.eventmanagement.service.security.AuthenticationService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventRegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventService eventService;
    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final AuthenticationService authenticationService;


    @Transactional
    public void registrationForAnEvent(@PositiveOrZero(message = "Id must be positive or equal to 0")
                                       Long eventId) {

        var user = authenticationService.getCurrentUserAuthenticated().getUser();
        var eventById = eventService.getEventById(eventId);
        if (user.id().equals(eventById.ownerId())) {
            throw new IllegalArgumentException("User is the creator of the event, so registration for it is not possible");
        }
        if (isEventStatusNotWaitStart(eventById)) {
            throw new IllegalArgumentException("Registration for the event with ID:%s is closed,its status is not 'WAIT_START' ".formatted(eventId));
        }
        var userRegistrationEntity = userMapper.mapFromUserToEntity(user);
        var eventEntity = eventMapper.mapFromEventToEntity(eventById);
        if (eventEntity.getMaxPlaces() <= eventEntity.getEventsRegistrationEntities().size()) {
            throw new IllegalArgumentException("no vacancies");
        }
        if (registrationRepository.existsByEventAndUser(eventEntity, userRegistrationEntity)) {
            throw new IllegalArgumentException("the user is already registered for the event");
        }
        var eventsRegistrationEntity = new RegistrationEntity(null, eventEntity, userRegistrationEntity);
        registrationRepository.save(eventsRegistrationEntity);

    }


    @Transactional
    public void cancelUserRegistration(@PositiveOrZero(message = "Id must be positive or equal to 0")
                                       Long eventId) {
        var eventById = eventService.getEventById(eventId);
        if (isEventStatusNotWaitStart(eventById)) {
            throw new IllegalArgumentException("It is not possible to unregister because the event with ID %s does not have the status 'WAIT_START' ".formatted(eventId));
        }
        var user = authenticationService.getCurrentUserAuthenticated().getUser();
        var registration = registrationRepository.findByEventAndUser(eventId, user.id())
                .orElseThrow(() -> new EntityNotFoundException("Registration not found"));
        registrationRepository.deleteRegistrationById(registration.getId());

    }

    private boolean isEventStatusNotWaitStart(Event eventById) {
        return eventById.status() != EventStatus.WAIT_START;
    }

}
