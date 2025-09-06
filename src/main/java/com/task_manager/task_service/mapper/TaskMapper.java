package com.task_manager.task_service.mapper;


import com.task_manager.auth_service.model.entity.User;
import com.task_manager.task_service.model.dto.TaskRequestDto;
import com.task_manager.task_service.model.dto.TaskResponseDto;
import com.task_manager.task_service.model.entity.Task;
import com.task_manager.task_service.model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(source = "assignedTo.id", target = "assignedTo")
    @Mapping(source = "team.id", target = "team")
    TaskResponseDto toResponseDto(Task task);

    @Mapping(source = "createdBy", target = "createdBy.id")
    @Mapping(source = "assignedTo", target = "assignedTo.id")
    @Mapping(source = "team", target = "team.id")
    Task toModel(TaskRequestDto dto);

    default Long mapUserToId(User user) {
        if (user == null) return null;
        return user.getId();
    }

    default Long mapTeamToId(Team team) {
        if (team == null) return null;
        return team.getId();
    }

}
