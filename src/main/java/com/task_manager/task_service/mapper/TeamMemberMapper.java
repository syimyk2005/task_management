package com.task_manager.task_service.mapper;

import com.task_manager.auth_service.model.entity.User;
import com.task_manager.task_service.model.dto.TeamMemberResponseDto;
import com.task_manager.task_service.model.entity.Team;
import com.task_manager.task_service.model.entity.TeamMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMemberMapper {

    TeamMemberResponseDto toResponseDto(TeamMember teamMember);

    default Long mapUserToId(User user) {
        if (user == null) return null;
        return user.getId();
    }

    default Long mapTeamToId(Team team) {
        if (team == null) return null;
        return team.getId();
    }

}
