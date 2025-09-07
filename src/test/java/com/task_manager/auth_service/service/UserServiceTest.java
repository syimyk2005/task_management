package com.task_manager.auth_service.service;

import com.task_manager.auth_service.mapper.UserMapper;
import com.task_manager.auth_service.model.dto.UserRequestDto;
import com.task_manager.auth_service.model.dto.UserResponseDto;
import com.task_manager.auth_service.model.entity.User;
import com.task_manager.auth_service.model.enums.Role;
import com.task_manager.auth_service.repository.TokenRepository;
import com.task_manager.auth_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private TokenRepository tokenRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        tokenRepository = mock(TokenRepository.class);
        userService = new UserService(userRepository, userMapper, tokenRepository);
    }

    @Test
    void getUserById_UserExists_ReturnsDto() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        UserResponseDto dto = new UserResponseDto(1L,
                "test",
                "email@test.com",
                "John",
                "Doe",
                Role.USER,
                Instant.now(),
                Instant.now()  );
        dto.setId(1L);
        dto.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        UserResponseDto result = userService.getUserById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getAllUsers_ReturnsListOfDtos() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        UserResponseDto dto1 = new UserResponseDto(1L,
                "test",
                "email@test.com",
                "John",
                "Doe",
                Role.USER,
                Instant.now(),
                Instant.now()  );
        dto1.setId(1L);
        dto1.setUsername("user1");

        UserResponseDto dto2 = new UserResponseDto(1L,
                "test",
                "email@test.com",
                "John",
                "Doe",
                Role.USER,
                Instant.now(),
                Instant.now()  );
        dto2.setId(2L);
        dto2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        List<UserResponseDto> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    void updateUser_UserExists_ReturnsUpdatedDto() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUser");

        UserRequestDto updatedRequest = new UserRequestDto();
        updatedRequest.setUsername("newUser");
        updatedRequest.setEmail("email@test.com");
        updatedRequest.setFirstname("First");
        updatedRequest.setLastname("Last");
        updatedRequest.setRole(Role.valueOf("USER"));

        UserResponseDto updatedDto = new UserResponseDto(1L,
                "test",
                "email@test.com",
                "John",
                "Doe",
                Role.USER,
                Instant.now(),
                Instant.now()  );
        updatedDto.setId(1L);
        updatedDto.setUsername("newUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toDto(existingUser)).thenReturn(updatedDto);

        UserResponseDto result = userService.updateUser(1L, updatedRequest);

        assertThat(result.getUsername()).isEqualTo("newUser");
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        UserRequestDto updatedRequest = new UserRequestDto();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUser(1L, updatedRequest));
    }

    @Test
    void deleteById_CallsRepositories() {
        userService.deleteById(1L);

        verify(tokenRepository, times(1)).deleteTokenByUserId(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
