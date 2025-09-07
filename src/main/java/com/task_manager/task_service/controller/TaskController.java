package com.task_manager.task_service.controller;

import com.task_manager.task_service.model.dto.TaskRequestDto;
import com.task_manager.task_service.model.dto.TaskResponseDto;
import com.task_manager.task_service.model.enums.Status;
import com.task_manager.task_service.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Тапшырмаларды башкаруу. (CRUD жана башка функциялар)")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Колдонуучулар тизмесин алуу.", description = "Тапшырмалардын тизмесин кайтарат жана статус, приритет, топ боюнча фильтрацию мумкунчулугун берет.")
    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAllTasks(
            @Parameter(description = "Пагинация параметрлери") Pageable pageable,
            @Parameter(description = "Статус боюнча фильтр") @RequestParam(required = false) String status,
            @Parameter(description = "Приоритет боюнча фильтр") @RequestParam(required = false) Integer priority,
            @Parameter(description = "Id боюнча фильтр") @RequestParam(required = false) Long teamId
    ) {
        Page<TaskResponseDto> tasks = taskService.getAllTasks(pageable, status, priority, teamId);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "ID боюнча тапшырманы алуу", description = "Тапшырманын идентификатору боюнча маалыматтарды кайтарат")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(
            @Parameter(description = "Тапшырманын ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @Operation(summary = "Жаңы тапшырма түзүү", description = "Берилген маалыматтар менен жаңы тапшырма түзөт")
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @Parameter(description = "Жаңы тапшырманын маалыматтары") @Valid @RequestBody TaskRequestDto newTask
    ) {
        return ResponseEntity.ok(taskService.createTask(newTask));
    }

    @Operation(summary = "Тапшырманы жаңыртуу", description = "Бар болгон тапшырманы ID боюнча жаңыртат")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @Parameter(description = "Тапшырманын ID") @PathVariable Long id,
            @Parameter(description = "Тапшырманын жаңы маалыматтары") @RequestBody TaskRequestDto updatedTask
    ) {
        return ResponseEntity.ok(taskService.updateTask(id, updatedTask));
    }

    @Operation(summary = "Тапшырманы өчүрүү", description = "ID боюнча тапшырманы өчүрөт")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Тапшырманын ID") @PathVariable Long id
    ) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Тапшырманын статусун жаңыртуу", description = "ID боюнча тапшырманын статусун өзгөртөт")
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateStatus(
            @Parameter(description = "Тапшырманын ID") @PathVariable Long id,
            @Parameter(description = "Жаңы статус") @RequestParam Status status
    ) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    @Operation(summary = "Тапшырманы колдонуучуга тапшыруу", description = "Тапшырманы ID боюнча колдонуучуга берүүнү камсыздайт")
    @PatchMapping("/{id}/assign")
    public ResponseEntity<TaskResponseDto> assignTask(
            @Parameter(description = "Тапшырманын IDси") @PathVariable Long id,
            @Parameter(description = "Тапшырманы алган колдонуучунун IDси") @RequestParam Long userId
    ) {
        return ResponseEntity.ok(taskService.assignTask(id, userId));
    }
}
