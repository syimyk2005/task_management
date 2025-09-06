package com.task_manager.task_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberResponseDto {

    private Long id;
    private Long team;
    private Long user;
    private Instant joinedAt;

}
