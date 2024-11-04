package ru.javacourse.eventmanagement.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javacourse.eventmanagement.db.entity.event.EventEntity;
import ru.javacourse.eventmanagement.db.entity.event.RegistrationEntity;
import ru.javacourse.eventmanagement.db.entity.user.UserEntity;
import ru.javacourse.eventmanagement.domain.event.EventStatus;
import ru.javacourse.eventmanagement.domain.service.security.AuthenticationService;
import ru.javacourse.eventmanagement.web.dto.event.EventUpdateRequestDto;
import ru.javacourse.eventmanagement.web.kafka.EventChangeKafkaMessage;
import ru.javacourse.eventmanagement.web.kafka.FieldChangeDto;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KafkaEventSentService {
    private final AuthenticationService authenticationService;
    private final KafkaTemplate<String, EventChangeKafkaMessage> kafkaTemplate;


    public void sendMessageDeleteEvent(EventEntity eventEntity) {
        var usersRegistrationId = getListWithUserRegistrationId(eventEntity);
        var oldStatus = eventEntity.getStatus();
        var newStatus = EventStatus.CANCELLED;
        var eventChangeKafkaMessage = EventChangeKafkaMessage.builder()
                .usersId(usersRegistrationId)
                .eventId(eventEntity.getId())
                .ownerId(eventEntity.getOwnerUser().getId())
                .changedById(getUserIdWhoChangedEvent())
                .status(new FieldChangeDto<>(oldStatus.toString(), newStatus.toString()))
                .build();
        kafkaSendMessage(eventChangeKafkaMessage);
    }

    public void kafkaSendMessage(EventChangeKafkaMessage eventChangeKafkaMessage) {
        var future = kafkaTemplate.send("ru.queuing.event_notification.event_change.read", "event", eventChangeKafkaMessage);
        future.whenComplete((messageSendResult, throwable) -> {
            if (throwable != null) {
                log.error(throwable.getMessage());
            } else {
                log.info("Notification sent to user {}", messageSendResult.getRecordMetadata());
            }
        });
    }

    private List<Long> getListWithUserRegistrationId(EventEntity eventEntity) {
        return eventEntity.getEventsRegistrationEntities()
                .stream()
                .map(RegistrationEntity::getUser)
                .map(UserEntity::getId)
                .toList();
    }


    private Long getUserIdWhoChangedEvent() {
        return authenticationService.getCurrentUserAuthenticated()
                .getUser()
                .id();
    }


    public EventChangeKafkaMessage createEventChangeKafkaMessage(EventEntity eventEntity,EventStatus oldStatus,EventStatus newStatus) {
        return EventChangeKafkaMessage.builder()
                .usersId(getListWithUserRegistrationId(eventEntity))
                .eventId(eventEntity.getId())
                .ownerId(eventEntity.getOwnerUser().getId())
                .status(new FieldChangeDto<>(oldStatus.toString(), newStatus.toString()))
                .build();
    }

    public EventChangeKafkaMessage createEventChangeKafkaMessage(EventEntity eventEntity, EventUpdateRequestDto eventDtoUpdate) {
        var usersRegistrationId = getListWithUserRegistrationId(eventEntity);
        FieldChangeDto<String> name = null;
        FieldChangeDto<Integer> maxPlaces = null;
        FieldChangeDto<LocalDateTime> date = null;
        FieldChangeDto<BigDecimal> cost = null;
        FieldChangeDto<Integer> duration = null;
        FieldChangeDto<Long> locationId = null;

        if (eventDtoUpdate.name() != null && !eventDtoUpdate.name().equals(eventEntity.getName())) {
            name = new FieldChangeDto<>(eventEntity.getName(), eventDtoUpdate.name());
        }
        if (eventDtoUpdate.maxPlaces() != null && !eventDtoUpdate.maxPlaces().equals(eventEntity.getMaxPlaces())) {
            maxPlaces = new FieldChangeDto<>(eventEntity.getMaxPlaces(), eventDtoUpdate.maxPlaces());
        }
        if (eventDtoUpdate.date() != null && !eventDtoUpdate.date().equals(eventEntity.getDate())) {
            date = new FieldChangeDto<>(eventEntity.getDate(), eventDtoUpdate.date());
        }
        if (eventDtoUpdate.cost() != null && !eventDtoUpdate.cost().equals(eventEntity.getCost())) {
            cost = new FieldChangeDto<>(eventEntity.getCost(), eventDtoUpdate.cost());
        }
        if (eventDtoUpdate.duration() != null && !eventDtoUpdate.duration().equals(eventEntity.getDuration())) {
            duration = new FieldChangeDto<>(eventEntity.getDuration(), eventDtoUpdate.duration());
        }
        if (eventDtoUpdate.locationId() != null && !eventDtoUpdate.locationId().equals(eventEntity.getLocation().getId())) {
            locationId = new FieldChangeDto<>(eventEntity.getLocation().getId(), eventDtoUpdate.locationId());
        }
        return EventChangeKafkaMessage.builder()
                .usersId(usersRegistrationId)
                .eventId(eventEntity.getId())
                .ownerId(eventEntity.getOwnerUser().getId())
                .changedById(getUserIdWhoChangedEvent())
                .name(name)
                .maxPlaces(maxPlaces)
                .date(date)
                .cost(cost)
                .duration(duration)
                .locationId(locationId)
                .build();
    }

}
