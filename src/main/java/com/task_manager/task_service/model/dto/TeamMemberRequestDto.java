package com.task_manager.task_service.model.dto;

import com.task_manager.auth_service.model.entity.User;
import com.task_manager.task_service.model.entity.Team;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberRequestDto {

    @NotNull(message = "field of user can't be empty")
    private Long userId;

}
