package com.task_manager.task_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.task_service.model.dto.TaskRequestDto;
import com.task_manager.task_service.model.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskRequestDto buildTask(String title) {
        TaskRequestDto task = new TaskRequestDto();
        task.setTitle(title);
        task.setDescription("Test description");
        task.setPriority(1);
        task.setStatus("NEW");
        task.setTeam(1L);
        return task;
    }

    private Long createTaskAndGetId(String title) throws Exception {
        TaskRequestDto task = buildTask(title);
        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseJson).get("id").asLong();
    }

    @Test
    void createAndGetTask_shouldReturnTask() throws Exception {
        String title = "Task " + UUID.randomUUID();
        Long taskId = createTaskAndGetId(title);

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title));
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        String title = "Task " + UUID.randomUUID();
        Long taskId = createTaskAndGetId(title);

        TaskRequestDto updatedTask = buildTask("Updated " + title);
        updatedTask.setPriority(2);

        mockMvc.perform(put("/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated " + title))
                .andExpect(jsonPath("$.priority").value(2));
    }

    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {
        String title = "Task " + UUID.randomUUID();
        Long taskId = createTaskAndGetId(title);

        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isNoContent());
    }
}
