package ru.javacourse.eventmanagement.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.javacourse.eventmanagement.domain.event.Event;
import ru.javacourse.eventmanagement.domain.event.EventStatus;
import ru.javacourse.eventmanagement.domain.locations.Location;
import ru.javacourse.eventmanagement.domain.mapper.EventMapper;
import ru.javacourse.eventmanagement.domain.service.EventService;
import ru.javacourse.eventmanagement.domain.service.LocationService;
import ru.javacourse.eventmanagement.domain.service.UserService;
import ru.javacourse.eventmanagement.web.dto.event.EventCreateRequestDto;
import ru.javacourse.eventmanagement.web.dto.event.EventDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EventService eventService;
    @MockBean
    private EventMapper eventMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private LocationService  locationService;

    private EventCreateRequestDto eventCreateRequestDto;
    private EventDto eventDto;
    private Event event;
    private Location eventLocation;


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
                Collections.emptySet()
        );
        this.eventLocation = new Location(1L, "Sunny Conference Room",
                "456 Ocean Drive, Seaside Town",
                50,
                "A bright and airy conference room with a stunning ocean view, ideal for medium-sized meetings.");

    }

//    @Test
//    @SneakyThrows
//    @WithMockUser(roles = "USER")
//    void createEvent_shouldReturnCreatedEventWithStatus201() {
//        UserDetails userDetails = Mockito.mock(UserDetails.class);
//        var user = new User(1L, "name", "password", 25, Role.ROLE_USER);
//        when(userDetails.getUsername()).thenReturn(user.login());
//        when(locationService.getLocationByID(1L)).thenReturn(eventLocation);
//        doReturn(user).when(userService).getUserByLogin(anyString());
//        when(eventMapper.mapFromRequestDtoToEvent(eventRequestDto)).thenReturn(event);
//        when(eventService.createEvent(event, user.login())).thenReturn(event);
//        when(eventMapper.mapFromEventToDto(event)).thenReturn(eventDto);
//        mockMvc.perform(post("/events")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(eventRequestDto))
//                        .principal(() -> user.login()))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(eventDto.id()))
//                .andExpect(jsonPath("$.name").value(eventDto.name()))
//                .andExpect(jsonPath("$.ownerId").value(eventDto.ownerId()))
//                .andExpect(jsonPath("$.maxPlaces").value(eventDto.maxPlaces()))
//                .andExpect(jsonPath("$.occupiedPlaces").value(eventDto.occupiedPlaces()))
//                .andExpect(jsonPath("$.date").value(eventDto.date().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
//                .andExpect(jsonPath("$.status").value(eventDto.status().toString()))
//                .andExpect(jsonPath("$.cost").value(eventDto.cost()))
//                .andExpect(jsonPath("$.duration").value(eventDto.duration()))
//                .andExpect(jsonPath("$.locationId").value(eventDto.locationId()));
//    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void createEvent_shouldReturnCreatedEventWithStatus401() {
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventCreateRequestDto)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    void createEvent_shouldReturnCreatedEventWithStatus403() {
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventCreateRequestDto)))
                .andExpect(status().isForbidden());

    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void createEvent_shouldReturnCreatedEventWithStatus400() {
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EventCreateRequestDto(
                                "",
                                eventCreateRequestDto.maxPlaces(),
                                eventCreateRequestDto.date(),
                                eventCreateRequestDto.cost(),
                                eventCreateRequestDto.duration(),
                                eventCreateRequestDto.locationId()
                        ))))
                .andExpect(status().isBadRequest());

    }
}