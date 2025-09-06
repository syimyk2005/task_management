package com.task_manager.task_service.model.dto;

import com.task_manager.auth_service.model.entity.User;
import com.task_manager.task_service.model.entity.Team;
import com.task_manager.task_service.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Integer priority;
    private String category;
    private Long createdBy;
    private Long assignedTo;
    private Long team;
    private LocalDateTime deadline;
    private Instant createdAt;
    private Instant updatedAt;

}
