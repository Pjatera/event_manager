package ru.javacourse.eventmanagement.locations;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;
import ru.javacourse.eventmanagement.domain.exeptions.NotFoundLocation;
import ru.javacourse.eventmanagement.domain.locations.Location;
import ru.javacourse.eventmanagement.entity.location.LocationEntity;
import ru.javacourse.eventmanagement.repository.LocationRepository;
import ru.javacourse.eventmanagement.service.LocationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class LocationServiceTest {
    @MockBean
    private LocationRepository locationRepository;
    private final LocationService locationService;
    private final List<LocationEntity> eventLocationEntities = new ArrayList<>();


    @BeforeEach
    void setUp() {
        var icePalace = new LocationEntity(42L,
                "Ледовый дворец",
                "г. СПб, Пятилеток 1",
                12300,
                "Спортивно-концертный комплекс в Санкт-Петербурге");
        var grandHall = new LocationEntity(43L,
                "Grand Hall",
                "123 Main St, Springfield",
                500,
                "A large event hall suitable for conferences and banquets.");
        eventLocationEntities.add(icePalace);
        eventLocationEntities.add(grandHall);
    }


    @Test
    void findAll() {
        when(locationRepository.findAll()).thenReturn(eventLocationEntities);
        var all = locationService.findAll();
        assertAll(
                () -> assertThat(all).isNotNull(),
                () -> assertThat(all).hasSize(eventLocationEntities.size())
        );
        verify(locationRepository, times(1)).findAll();

    }

    @Test
    void createNewLocation_created() {
        var eventLocationEntity = new LocationEntity(44L, "Sunny Conference Room",
                "456 Ocean Drive, Seaside Town",
                50,
                "A bright and airy conference room with a stunning ocean view, ideal for medium-sized meetings.");
        var eventLocationSource = new Location(44L, "Sunny Conference Room",
                "456 Ocean Drive, Seaside Town",
                50,
                "A bright and airy conference room with a stunning ocean view, ideal for medium-sized meetings.");
        var newEventLocation = new Location(null, "Sunny Conference Room",
                "456 Ocean Drive, Seaside Town",
                50,
                "A bright and airy conference room with a stunning ocean view, ideal for medium-sized meetings.");

        when(locationRepository.save(any(LocationEntity.class))).thenReturn(eventLocationEntity);
        var newLocationCreated = locationService.createNewLocation(newEventLocation);
        assertAll(
                () -> assertThat(newLocationCreated).isNotNull(),
                () -> assertThat(newLocationCreated).isEqualTo(eventLocationSource)
        );
        verify(locationRepository, times(1)).save(any(LocationEntity.class));

    }

    @Test
    void createNewLocation_shouldThrowException() {

        assertThrows(ConstraintViolationException.class, () -> locationService.createNewLocation(null));
        assertThrows(ConstraintViolationException.class, () -> locationService.createNewLocation(new Location(44L, "Sunny Conference Room",
                "456 Ocean Drive, Seaside Town",
                50,
                "A bright and airy conference room with a stunning ocean view, ideal for medium-sized meetings.")));
        assertThrows(ConstraintViolationException.class, () -> locationService.createNewLocation(new Location(null, "Sunny Conference",
                "",
                50,
                "A bright and airy conference room with a stunning ocean view, ideal for medium-sized meetings.")));
        assertThrows(ConstraintViolationException.class, () -> locationService.createNewLocation(new Location(null, "",
                "456 Ocean Drive, Seaside Town",
                50,
                "A bright and airy conference room with a stunning ocean view, ideal for medium-sized meetings.")));
        assertThrows(ConstraintViolationException.class, () -> locationService.createNewLocation(new Location(null, "Sunny Conference Room",
                "456 Ocean Drive, Seaside Town",
                4,
                "A bright and airy conference room with a stunning ocean view, ideal for medium-sized meetings.")));

        verify(locationRepository, times(0)).save(any(LocationEntity.class));

    }

    @Test
    void deleteLocation_successfully() {
        long locationId = 42L;
        var sunnyConferenceRoom = eventLocationEntities.getFirst();
        doReturn(Optional.of(sunnyConferenceRoom)).when(locationRepository).findById(locationId);
        doNothing().when(locationRepository).deleteById(locationId);
        var location = locationService.deleteLocation(locationId);
        assertThat(location).isNotNull();
        assertThat(location.id()).isEqualTo(locationId);
        verify(locationRepository, times(1)).deleteById(locationId);
        verify(locationRepository, times(1)).findById(locationId);
    }


    @Test
    void getLocationByID() {
        var locationEntity = eventLocationEntities.getFirst();
        var locationId = locationEntity.getId();
        doReturn(Optional.of(locationEntity)).when(locationRepository).findById(locationId);
        var location = locationService.getLocationByID(locationId);
        assertThat(location).isNotNull();
        assertThat(location.id()).isEqualTo(locationId);
        verify(locationRepository, times(1)).findById(locationId);
    }

    @Test
    void getLocationByID_shouldThrowException() {

        doReturn(Optional.ofNullable(null)).when(locationRepository).findById(anyLong());
        assertThrows(NotFoundLocation.class, () -> locationService.getLocationByID(anyLong()));
        verify(locationRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateById() {
        var locationEntity = eventLocationEntities.getFirst();
        var locationId = locationEntity.getId();
        var locationUpdate = new Location(null, "UpdateName",
                "г. СПб, Пятилеток 1",
                12300,
                "Спортивно-концертный комплекс в Санкт-Петербурге");
        doReturn(Optional.of(locationEntity)).when(locationRepository).findById(locationId);
        doReturn(locationEntity).when(locationRepository).save(any(LocationEntity.class));
        var location = locationService.updateById(locationId, locationUpdate);
        assertThat(location).isNotNull();
        assertThat(location.id()).isEqualTo(locationId);
        verify(locationRepository, times(1)).findById(locationId);
        verify(locationRepository, times(1)).save(any(LocationEntity.class));
    }

    @Test
    void updateById_shouldThrowException() {
        var locationEntity = eventLocationEntities.getFirst();
        var locationId = locationEntity.getId();

        var locationUpdate = new Location(null, "UpdateName",
                "г. СПб, Пятилеток 1",
                100,
                "Спортивно-концертный комплекс в Санкт-Петербурге");
        doReturn(Optional.of(locationEntity)).when(locationRepository).findById(locationId);
        doReturn(locationEntity).when(locationRepository).save(any(LocationEntity.class));
        assertThrows(IllegalArgumentException.class, () -> locationService.updateById(locationId, locationUpdate));
        verify(locationRepository, times(1)).findById(locationId);
        verify(locationRepository, times(0)).save(any(LocationEntity.class));
    }

}