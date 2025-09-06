package com.task_manager.task_service.model.dto;
import com.task_manager.task_service.model.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {

    @NotNull(message = "you should write some title")
    private String title;
    private String description;
    @NotNull(message = "status can't be empty")
    private String status;
    @NotNull(message = "priority can't be empty")
    private Integer priority;
    private String category;
    private Long createdBy;
    private Long assignedTo;
    private Long team;
    private LocalDateTime deadline;

}
