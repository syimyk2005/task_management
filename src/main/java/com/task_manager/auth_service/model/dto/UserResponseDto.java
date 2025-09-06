package com.task_manager.auth_service.model.dto;

import com.task_manager.auth_service.model.entity.Token;
import com.task_manager.auth_service.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
}

