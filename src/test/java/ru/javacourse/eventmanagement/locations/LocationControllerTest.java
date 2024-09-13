package ru.javacourse.eventmanagement.locations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest

@AutoConfigureMockMvc
class LocationControllerTest {
    @MockBean
    private LocationRepository locationRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final LocationMapper locationMapper;
    private final List<LocationEntity> eventLocationEntities = new ArrayList<>();
    private final LocationEntity firstEntity;
    private final Location locationCheck;
    private final LocationDto locationDtoCheck;


    @Autowired
    public LocationControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, LocationMapper locationMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.locationMapper = locationMapper;

        var icePalace = new LocationEntity(42,
                "Ледовый дворец",
                "г. СПб, Пятилеток 1",
                12300,
                "Спортивно-концертный комплекс в Санкт-Петербурге");
        var grandHall = new LocationEntity(43,
                "Grand Hall",
                "123 Main St, Springfield",
                500,
                "A large event hall suitable for conferences and banquets.");
        eventLocationEntities.add(icePalace);
        eventLocationEntities.add(grandHall);

        this.firstEntity = icePalace;
        this.locationCheck = locationMapper.mapFromEntity(icePalace);
        this.locationDtoCheck = locationMapper.mapToDto(locationCheck);
        objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
    }


    @Test
    void getLocations() throws Exception {
        doReturn(eventLocationEntities).when(locationRepository).findAll();
        var contentAsString = mockMvc.perform(get("/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(eventLocationEntities.getFirst().getName()))
                .andExpect(jsonPath("$[0].address").value(eventLocationEntities.getFirst().getAddress()))
                .andExpect(jsonPath("$[0].capacity").value(eventLocationEntities.getFirst().getCapacity()))
                .andExpect(jsonPath("$[0].description").value(eventLocationEntities.getFirst().getDescription()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        var listResult = Arrays.asList(objectMapper.readValue(contentAsString, LocationDto[].class));

        var listDtoCheck = eventLocationEntities.stream().map(locationMapper::mapFromEntity).map(locationMapper::mapToDto).toList();

        assertThat(listResult).isEqualTo(listDtoCheck);
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void createLocation() throws Exception {
        var locationDtoRequest = new LocationDto(null, locationDtoCheck.name(), locationDtoCheck.address(), locationDtoCheck.capacity(), locationDtoCheck.description());
        doReturn(firstEntity).when(locationRepository).save(any(LocationEntity.class));
        var contentAsString = mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDtoRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        var locationDto = objectMapper.readValue(contentAsString, LocationDto.class);
        assertThat(locationDto).isEqualTo(locationDtoCheck);
        verify(locationRepository, times(1)).save(any(LocationEntity.class));
    }

    @Test
    void deleteLocation() throws Exception {
        doReturn(Optional.of(firstEntity)).when(locationRepository).findById(firstEntity.getId());
        doNothing().when(locationRepository).deleteById(firstEntity.getId());
        var contentAsString = mockMvc.perform(delete("/locations/{id}", firstEntity.getId()))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString();
        var locationDto = objectMapper.readValue(contentAsString, LocationDto.class);
        assertThat(locationDto).isEqualTo(locationDtoCheck);
        verify(locationRepository, times(1)).findById(firstEntity.getId());
        verify(locationRepository, times(1)).deleteById(firstEntity.getId());
    }

    @Test
    void getLocationById() throws Exception {

    }

    @Test
    void updateLocationByID() throws Exception {
    }

    
    @Test
void shouldReturn200WhenUpdatingAnExistingLocation() throws Exception {
    var locationId = 42;
    var updatedLocationDto = new LocationDto(null, "Updated Name", "Updated Address", 15000, "Updated Description");
    doReturn(Optional.of(firstEntity)).when(locationRepository).findById(locationId);
    doReturn(firstEntity).when(locationRepository).save(any(LocationEntity.class));

    var contentAsString = mockMvc.perform(put("/locations/{id}", locationId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedLocationDto)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    var locationDto = objectMapper.readValue(contentAsString, LocationDto.class);
    assertThat(locationDto.name()).isEqualTo(updatedLocationDto.name());
    verify(locationRepository, times(1)).findById(locationId);
    verify(locationRepository, times(1)).save(any(LocationEntity.class));
}
}