package com.task_manager.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.auth_service.model.dto.UserRequestDto;
import com.task_manager.auth_service.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzeWlteWsxMTExMSIsImlhdCI6MTc1NzE1MzI0OCwiZXhwIjoxNzU3MjM5NjQ4fQ.PbtvvU629ktyfY6RNKgK2F3zakiETjlTqIqXoDvfBWjw8dZBe_msxDe__MLAgCwHckxR8xPVYd2fW43nmYC60g";

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        Long userId = 1L;
        mockMvc.perform(get("/api/users/{id}", userId)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        Long userId = 1L;
        var userUpdate = new UserRequestDto();
        userUpdate.setUsername("updatedUser");
        userUpdate.setEmail("updated@example.com");
        userUpdate.setPassword("testpass");
        userUpdate.setRole(Role.USER);
        userUpdate.setFirstname("Test");
        userUpdate.setLastname("User");

        mockMvc.perform(put("/api/users/{id}", userId)
                        .header("Authorization", "Bearer " + TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        Long userId = 1L;
        mockMvc.perform(delete("/api/users/{id}", userId)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isNoContent());
    }

}
