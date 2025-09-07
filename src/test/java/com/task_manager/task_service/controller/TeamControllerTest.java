package com.task_manager.task_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.task_service.model.dto.TeamMemberRequestDto;
import com.task_manager.task_service.model.dto.TeamMemberResponseDto;
import com.task_manager.task_service.model.dto.TeamRequestDto;
import com.task_manager.task_service.model.dto.TeamResponseDto;
import com.task_manager.task_service.service.TeamMemberService;
import com.task_manager.task_service.service.TeamService;
import com.task_manager.auth_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TeamControllerTest {

    private MockMvc mockMvc;
    private TeamService teamService;
    private TeamMemberService teamMemberService;
    private UserRepository userRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        teamService = Mockito.mock(TeamService.class);
        teamMemberService = Mockito.mock(TeamMemberService.class);
        userRepository = Mockito.mock(UserRepository.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new TeamController(teamService, teamMemberService, userRepository)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllTeams() throws Exception {
        TeamResponseDto team = new TeamResponseDto();
        when(teamService.findAll()).thenReturn(List.of(team));

        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetTeamById() throws Exception {
        TeamResponseDto team = new TeamResponseDto();
        when(teamService.getTeam(anyLong())).thenReturn(team);

        mockMvc.perform(get("/api/teams/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateTeam() throws Exception {
        TeamRequestDto request = new TeamRequestDto();
        request.setName("Test Team");
        request.setDescription("Test Description");
        request.setCreatedBy(1L); // важно указать валидный ID

        TeamResponseDto response = new TeamResponseDto();
        when(teamService.createTeam(any(TeamRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void testUpdateTeam() throws Exception {
        TeamRequestDto request = new TeamRequestDto();
        TeamResponseDto response = new TeamResponseDto();
        when(teamService.update(anyLong(), any(TeamRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteTeam() throws Exception {
        mockMvc.perform(delete("/api/teams/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetMembers() throws Exception {
        TeamMemberResponseDto member = new TeamMemberResponseDto();
        when(teamMemberService.getMembers(anyLong())).thenReturn(List.of(member));

        mockMvc.perform(get("/api/teams/1/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testAddMember() throws Exception {
        TeamMemberRequestDto request = new TeamMemberRequestDto();
        TeamMemberResponseDto response = new TeamMemberResponseDto();
        when(teamMemberService.addMember(anyLong(), any(TeamMemberRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/teams/1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveMember() throws Exception {
        mockMvc.perform(delete("/api/teams/1/members/2"))
                .andExpect(status().isNoContent());
    }
}
