package com.task_manager.auth_service.controller;

import com.task_manager.auth_service.model.dto.UserRequestDto;
import com.task_manager.auth_service.model.dto.UserResponseDto;
import com.task_manager.auth_service.model.enums.Role;
import com.task_manager.auth_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void allUsers_shouldReturnListOfUsers() {
        List<UserResponseDto> mockList = List.of(new UserResponseDto(1L,
                "test",
                "email@test.com",
                "John",
                "Doe",
                Role.USER,
                Instant.now(),
                Instant.now()));
        when(userService.getAllUsers()).thenReturn(mockList);

        ResponseEntity<List<UserResponseDto>> response = userController.allUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockList, response.getBody());
    }

    @Test
    void getUser_shouldReturnUserById() {
        UserResponseDto mockUser = new UserResponseDto(
                1L,
                "test",
                "email@test.com",
                "John",
                "Doe",
                Role.USER,
                Instant.now(),
                Instant.now()
        );
        when(userService.getUserById(1L)).thenReturn(mockUser);

        ResponseEntity<UserResponseDto> response = userController.getUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("updated");
        request.setEmail("updated@test.com");
        request.setFirstname("Jane");
        request.setLastname("Doe");
        request.setRole(Role.USER);

        UserResponseDto updatedUser = new UserResponseDto(
                1L,
                "test",
                "email@test.com",
                "John",
                "Doe",
                Role.USER,
                Instant.now(),
                Instant.now()
        );
        when(userService.updateUser(1L, request)).thenReturn(updatedUser);

        ResponseEntity<UserResponseDto> response = userController.updateUser(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void deleteUser_shouldReturnNoContent() {
        doNothing().when(userService).deleteById(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteById(1L);
    }
}
