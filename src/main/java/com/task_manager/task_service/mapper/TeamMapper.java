package com.task_manager.task_service.mapper;

import com.task_manager.auth_service.model.entity.User;
import com.task_manager.task_service.model.dto.TeamRequestDto;
import com.task_manager.task_service.model.dto.TeamResponseDto;
import com.task_manager.task_service.model.entity.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    Team toModel(TeamRequestDto dto);

    TeamResponseDto toResponseDto(Team team);

    default User mapUserId(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

    default Long mapUserToId(User user) {
        if (user == null) return null;
        return user.getId();
    }
}

