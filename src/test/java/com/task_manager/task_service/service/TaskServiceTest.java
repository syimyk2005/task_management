package com.task_manager.task_service.service;

import com.task_manager.auth_service.model.entity.User;
import com.task_manager.auth_service.repository.UserRepository;
import com.task_manager.task_service.exception.TaskNotFoundException;
import com.task_manager.task_service.mapper.TaskMapper;
import com.task_manager.task_service.model.dto.TaskRequestDto;
import com.task_manager.task_service.model.dto.TaskResponseDto;
import com.task_manager.task_service.model.entity.Task;
import com.task_manager.task_service.model.entity.Team;
import com.task_manager.task_service.model.enums.Status;
import com.task_manager.task_service.repository.TaskRepository;
import com.task_manager.task_service.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TeamRepository teamRepository;
    private TaskMapper taskMapper;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        userRepository = mock(UserRepository.class);
        teamRepository = mock(TeamRepository.class);
        taskMapper = mock(TaskMapper.class);
        taskService = new TaskService(taskRepository, userRepository, taskMapper, teamRepository);
    }

    @Test
    void createTask_Success() {
        TaskRequestDto dto = new TaskRequestDto();
        dto.setTitle("Task 1");
        dto.setCreatedBy(1L);
        dto.setAssignedTo(2L);
        dto.setTeam(1L);

        Task task = new Task();
        TaskResponseDto responseDto = new TaskResponseDto();

        User createdBy = new User(); createdBy.setId(1L);
        User assignedTo = new User(); assignedTo.setId(2L);
        Team team = new Team(); team.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(createdBy));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignedTo));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(taskMapper.toModel(dto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponseDto(task)).thenReturn(responseDto);

        TaskResponseDto result = taskService.createTask(dto);

        assertThat(result).isEqualTo(responseDto);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getTaskById_TaskExists_ReturnsDto() {
        Task task = new Task();
        TaskResponseDto dto = new TaskResponseDto();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponseDto(task)).thenReturn(dto);

        TaskResponseDto result = taskService.getTaskById(1L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getTaskById_TaskNotFound_ThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void updateStatus_TaskExists_UpdatesStatus() {
        Task task = new Task();
        TaskResponseDto dto = new TaskResponseDto();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponseDto(task)).thenReturn(dto);

        TaskResponseDto result = taskService.updateStatus(1L, Status.COMPLETED);

        assertThat(result).isEqualTo(dto);
        assertThat(task.getStatus()).isEqualTo(Status.COMPLETED);
    }

    @Test
    void assignTask_TaskAndUserExist_AssignsTask() {
        Task task = new Task();
        User user = new User();
        TaskResponseDto dto = new TaskResponseDto();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponseDto(task)).thenReturn(dto);

        TaskResponseDto result = taskService.assignTask(1L, 2L);

        assertThat(result).isEqualTo(dto);
        assertThat(task.getAssignedTo()).isEqualTo(user);
    }

    @Test
    void getAllTasks_ReturnsPagedDtos() {
        Task task1 = new Task();
        Task task2 = new Task();
        TaskResponseDto dto1 = new TaskResponseDto();
        TaskResponseDto dto2 = new TaskResponseDto();

        Page<Task> page = new PageImpl<>(List.of(task1, task2));
        when(taskRepository.findAllWithFilters(any(Pageable.class), any(), any(), any())).thenReturn(page);
        when(taskMapper.toResponseDto(task1)).thenReturn(dto1);
        when(taskMapper.toResponseDto(task2)).thenReturn(dto2);

        Page<TaskResponseDto> result = taskService.getAllTasks(Pageable.unpaged(), null, null, null);

        assertThat(result.getContent()).containsExactly(dto1, dto2);
    }
}
