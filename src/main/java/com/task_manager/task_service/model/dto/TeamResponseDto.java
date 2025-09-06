package com.task_manager.task_service.model.dto;

import com.task_manager.auth_service.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponseDto {

    private Long id;
    private String name;
    private String description;
    private Long createdBy;
    private Instant createdAt;
    private Instant updatedAt;

}
