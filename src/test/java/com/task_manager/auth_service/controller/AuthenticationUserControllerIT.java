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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDto buildUser(String email, String username) {
        UserRequestDto dto = new UserRequestDto();
        dto.setUsername(username);
        dto.setPassword("testpass");
        dto.setEmail(email);
        dto.setRole(Role.USER);
        dto.setFirstname("Test");
        dto.setLastname("User");
        return dto;
    }

    @Test
    void registerUser_shouldReturnTokens() throws Exception {
        String uniqueEmail = "user" + System.currentTimeMillis() + "@example.com";
        String uniqueUsername = "user" + System.currentTimeMillis();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUser(uniqueEmail, uniqueUsername))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void registerUser_withDuplicateEmail_shouldReturnConflict() throws Exception {
        String email1 = "user" + UUID.randomUUID() + "@example.com";
        String email2 = "user" + UUID.randomUUID() + "@example.com";
        String username1 = "user" + UUID.randomUUID();
        String username2 = "user" + UUID.randomUUID();


        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUser(email1, username1))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUser(email2, username2))))
                .andExpect(status().isOk());
    }
}
