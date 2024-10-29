package ru.javacourse.eventmanagement.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import ru.javacourse.eventmanagement.domain.event.Event;
import ru.javacourse.eventmanagement.domain.event.EventStatus;
import ru.javacourse.eventmanagement.domain.service.EventService;
import ru.javacourse.eventmanagement.domain.service.UserService;
import ru.javacourse.eventmanagement.web.controllers.UserController;
import ru.javacourse.eventmanagement.web.dto.event.EventDto;
import ru.javacourse.eventmanagement.web.dto.event.EventCreateRequestDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceTest {
    private final UserService userService;
    private final UserController userController;

    private final EventService eventService;
    private EventCreateRequestDto eventCreateRequestDto;
    private EventDto eventDto;
    private Event event;


    @BeforeEach
    public void setUp() {
        this.eventCreateRequestDto = new EventCreateRequestDto(
                "Лекция по Java",
                10,
                LocalDateTime.of(2025, Month.APRIL, 12, 20, 30, 00),
                BigDecimal.valueOf(1200),
                60,
                1L
        );
        this.eventDto = new EventDto(1L,
                eventCreateRequestDto.name(),
                2L,
                eventCreateRequestDto.maxPlaces(),
                0,
                eventCreateRequestDto.date(),
                eventCreateRequestDto.cost(),
                eventCreateRequestDto.duration(),
                eventCreateRequestDto.locationId(),
                EventStatus.WAIT_START
        );
        this.event = new Event(
                eventDto.id(),
                eventDto.name(),
                eventDto.ownerId(),
                eventDto.maxPlaces(),
                eventDto.date(),
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId(),
                eventDto.status(),
                Collections.emptySet());

    }


//    @Test
//   void qwe(){
//     List<Event> events = eventService.getAllUsersEvents("user123");
//    for (Event event : events) {
//        System.out.println(event);
//    }
//    }


}