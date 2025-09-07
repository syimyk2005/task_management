package com.task_manager.auth_service.mapper;


import com.task_manager.auth_service.model.dto.UserRequestDto;
import com.task_manager.auth_service.model.dto.UserResponseDto;
import com.task_manager.auth_service.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role")
    UserResponseDto toDto(User user);

    User toModel(UserRequestDto dto);

}





