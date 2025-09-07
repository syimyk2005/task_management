package com.task_manager.task_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.task_manager.task_service.model.dto.TaskRequestDto;
import com.task_manager.task_service.model.dto.TaskResponseDto;
import com.task_manager.task_service.model.enums.Status;
import com.task_manager.task_service.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;
    private TaskService taskService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskService = Mockito.mock(TaskService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(taskService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    @Test
    void testGetAllTasks() throws Exception {
        TaskResponseDto task = new TaskResponseDto();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(Status.COMPLETED);
        task.setPriority(1);
        task.setCategory("General");
        task.setCreatedBy(1L);
        task.setAssignedTo(2L);
        task.setTeam(1L);
        task.setDeadline(LocalDateTime.now());
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());


        when(taskService.getAllTasks(any(PageRequest.class), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(task)));

        task.setStatus(Status.IN_PROGRESS);

        mockMvc.perform(get("/api/tasks")
                        .param("page", "0")
                        .param("size", "10")
                        .param("status", "COMPLETED")
                        .param("priority", "1")
                        .param("teamId", "1"))
                .andExpect(status().isInternalServerError());


    }

    @Test
    void testGetTaskById() throws Exception {
        TaskResponseDto task = new TaskResponseDto();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setStatus(Status.NEW);
        when(taskService.getTaskById(anyLong())).thenReturn(task);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void testCreateTask() throws Exception {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Test Task");
        request.setDescription("Description");
        request.setStatus("NEW");
        request.setPriority(1);
        request.setCategory("General");
        request.setCreatedBy(1L);
        request.setAssignedTo(2L);
        request.setTeam(1L);

        TaskResponseDto response = new TaskResponseDto();
        response.setId(1L);
        response.setTitle(request.getTitle());
        response.setStatus(Status.NEW);

        when(taskService.createTask(any(TaskRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Updated Task");
        request.setDescription("Updated Description");
        request.setStatus("IN_PROGRESS");
        request.setPriority(2);
        request.setCategory("Bug");
        request.setCreatedBy(1L);
        request.setAssignedTo(2L);
        request.setTeam(1L);

        TaskResponseDto response = new TaskResponseDto();
        response.setId(1L);
        response.setTitle("Updated Task");
        response.setStatus(Status.IN_PROGRESS);

        when(taskService.updateTask(anyLong(), any(TaskRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateStatus() throws Exception {
        TaskResponseDto response = new TaskResponseDto();
        response.setId(1L);
        response.setStatus(Status.NEW);

        when(taskService.updateStatus(anyLong(), any(Status.class))).thenReturn(response);

        mockMvc.perform(patch("/api/tasks/1/status")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void testAssignTask() throws Exception {
        TaskResponseDto response = new TaskResponseDto();
        response.setId(1L);

        when(taskService.assignTask(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(patch("/api/tasks/1/assign")
                        .param("userId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
