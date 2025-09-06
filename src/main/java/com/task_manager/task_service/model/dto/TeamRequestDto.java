package com.task_manager.task_service.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamRequestDto {

    @NotBlank(message = "Name can't be empty")
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull(message = "createdById can't be null")
    private Long createdBy;
}
