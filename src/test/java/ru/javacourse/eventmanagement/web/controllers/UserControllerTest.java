package ru.javacourse.eventmanagement.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.javacourse.eventmanagement.domain.users.Role;
import ru.javacourse.eventmanagement.domain.users.User;
import ru.javacourse.eventmanagement.domain.users.UserMapper;
import ru.javacourse.eventmanagement.service.UserService;
import ru.javacourse.eventmanagement.web.dto.auth.UserRegistration;
import ru.javacourse.eventmanagement.web.dto.auth.UserRegistrationMapper;
import ru.javacourse.eventmanagement.web.dto.users.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserRegistrationMapper userRegistrationMapper;

    private UserRegistration userRegistration;
    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userRegistration = new UserRegistration("user21", "password", 30);
        user = new User(1L,
                "user21",
                null,
                30,
                Role.ROLE_USER);
        userDto = new UserDto(1L,
                "user21",
                30,
                Role.ROLE_USER);
    }


    @Test
    @WithAnonymousUser
    @SneakyThrows
    void registerUser_ShouldReturnCreatedUser() {

        when(userRegistrationMapper.mapFromUserRegistration(any(UserRegistration.class))).thenReturn(user);
        when(userService.registerUser(any(User.class))).thenReturn(user);
        when(userMapper.mapToDto(any(User.class))).thenReturn(userDto);


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistration)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.id()))
                .andExpect(jsonPath("$.login").value(user.login()));

        verify(userService).registerUser(any(User.class));
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void getUserById_ShouldReturnUserWithStatus401() {
        long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(user);
        when(userMapper.mapToDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(userService,never()).getUserById(userId);
        verify(userMapper, never()).mapToDto(any(User.class));
    }
    @Test
    @SneakyThrows
    @WithMockUser(roles = "USER")
    void getUserById_ShouldReturnUserWithStatus403() {
        long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(user);
        when(userMapper.mapToDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService,never()).getUserById(userId);
        verify(userMapper, never()).mapToDto(any(User.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @SneakyThrows
    void getUserById_ShouldReturnUserWithStatusCreated() {
        long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(user);
        when(userMapper.mapToDto(any(User.class))).thenReturn(userDto);


        var contentAsString = mockMvc.perform(get("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.login").value(userDto.login()))
                .andExpect(jsonPath("$.age").value(userDto.age()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var userDtoResponse = objectMapper.readValue(contentAsString, UserDto.class);

        assertThat(userDtoResponse).isEqualTo(userDto);

        verify(userService, times(1)).getUserById(userId);
        verify(userMapper, times(1)).mapToDto(any(User.class));
    }

}