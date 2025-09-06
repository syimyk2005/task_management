package com.task_manager.task_service.service;

import com.task_manager.auth_service.exception.UserNotFoundException;
import com.task_manager.task_service.exception.TaskNotFoundException;
import com.task_manager.task_service.exception.TeamNotFoundException;
import com.task_manager.task_service.mapper.TaskMapper;
import com.task_manager.task_service.model.dto.TaskRequestDto;
import com.task_manager.task_service.model.dto.TaskResponseDto;
import com.task_manager.task_service.model.entity.Task;
import com.task_manager.auth_service.model.entity.User;
import com.task_manager.task_service.model.entity.Team;
import com.task_manager.task_service.model.enums.Status;
import com.task_manager.task_service.repository.TaskRepository;
import com.task_manager.auth_service.repository.UserRepository;
import com.task_manager.task_service.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final TeamRepository teamRepository;

    public Page<TaskResponseDto> getAllTasks(Pageable pageable, String status, Integer priority, Long teamId) {
        return taskRepository.findAllWithFilters(pageable, status, priority, teamId)
                .map(taskMapper::toResponseDto);
    }


    public TaskResponseDto createTask(TaskRequestDto requestDto) {
        if (requestDto.getStatus() == null) {
            requestDto.setStatus(String.valueOf(Status.NEW));
        }

        Task task = taskMapper.toModel(requestDto);

        if (requestDto.getCreatedBy() != null) {
            task.setCreatedBy(userRepository.findById(requestDto.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }

        if (requestDto.getAssignedTo() != null) {
            task.setAssignedTo(userRepository.findById(requestDto.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }

        if (requestDto.getTeam() != null) {
            task.setTeam(teamRepository.findById(requestDto.getTeam())
                    .orElseThrow(() -> new RuntimeException("Team not found")));
        }

        task = taskRepository.save(task);
        return taskMapper.toResponseDto(task);
    }


    public TaskResponseDto getTaskById(Long id) {
        return taskMapper.toResponseDto(taskRepository.findById(id).orElseThrow( () -> new TaskNotFoundException("Task not found")));
    }

    public TaskResponseDto updateTask(Long id, TaskRequestDto requestDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        task.setTitle(requestDto.getTitle());
        task.setDescription(requestDto.getDescription());
        task.setStatus(Status.valueOf(requestDto.getStatus()));
        task.setPriority(requestDto.getPriority());
        task.setCategory(requestDto.getCategory());
        task.setDeadline(requestDto.getDeadline());
        User assignedTo = userRepository.findById(requestDto.getAssignedTo()).orElseThrow(() -> new UserNotFoundException("User not found"));
        User createdBy = userRepository.findById(requestDto.getCreatedBy()).orElseThrow(() -> new UserNotFoundException("User not found"));


        Team team = teamRepository.findById(requestDto.getTeam()).orElseThrow(() -> new TeamNotFoundException("Team not found"));
        task.setAssignedTo(assignedTo);
        task.setCreatedBy(createdBy);
        task.setTeam(team);
        return taskMapper.toResponseDto(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskResponseDto updateStatus(Long id, Status status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        task.setStatus(status);
        return taskMapper.toResponseDto(taskRepository.save(task));
    }

    public TaskResponseDto assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        task.setAssignedTo(user);
        return taskMapper.toResponseDto(taskRepository.save(task));
    }
}


