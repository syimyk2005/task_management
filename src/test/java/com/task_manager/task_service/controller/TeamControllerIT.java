package com.task_manager.task_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.task_service.model.dto.TeamMemberRequestDto;
import com.task_manager.task_service.model.dto.TeamRequestDto;
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
class TeamControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzeWlteWsxMTExMSIsImlhdCI6MTc1NzE1MzI0OCwiZXhwIjoxNzU3MjM5NjQ4fQ.PbtvvU629ktyfY6RNKgK2F3zakiETjlTqIqXoDvfBWjw8dZBe_msxDe__MLAgCwHckxR8xPVYd2fW43nmYC60g";



    private TeamRequestDto buildTeam(String name, Long creatorId) {
        TeamRequestDto team = new TeamRequestDto();
        team.setName(name);
        team.setCreatedBy(creatorId);
        return team;
    }

    private Long createTeamAndGetId(String name, Long creatorId) throws Exception {
        TeamRequestDto team = buildTeam(name, creatorId);
        MvcResult result = mockMvc.perform(post("/api/teams")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(team)))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    void createAndGetTeam_shouldReturnTeam() throws Exception {
        Long creatorId = 1L;
        String name = "Team " + UUID.randomUUID();
        Long teamId = createTeamAndGetId(name, creatorId);
        mockMvc.perform(get("/api/teams/{id}", teamId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    void updateTeam_shouldReturnUpdatedTeam() throws Exception {
        Long creatorId = 1L;
        String name = "Team " + UUID.randomUUID();
        Long teamId = createTeamAndGetId(name, creatorId);
        TeamRequestDto updatedTeam = buildTeam("Updated " + name, creatorId);
        mockMvc.perform(put("/api/teams/{id}", teamId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTeam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated " + name));
    }

    @Test
    void addAndRemoveMember_shouldWork() throws Exception {
        Long creatorId = 1L;
        String teamName = "Team " + UUID.randomUUID();
        Long teamId = createTeamAndGetId(teamName, creatorId);

        TeamMemberRequestDto member = new TeamMemberRequestDto();
        member.setUserId(1L);

        mockMvc.perform(post("/api/teams/{teamId}/members", teamId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value(1));

        mockMvc.perform(get("/api/teams/{teamId}/members", teamId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user").value(1));

        mockMvc.perform(delete("/api/teams/{teamId}/members/{userId}", teamId, 1L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }


    @Test
    void deleteTeam_shouldReturnNoContent() throws Exception {
        Long creatorId = 1L;
        String name = "Team " + UUID.randomUUID();
        Long teamId = createTeamAndGetId(name, creatorId);
        mockMvc.perform(delete("/api/teams/{id}", teamId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
