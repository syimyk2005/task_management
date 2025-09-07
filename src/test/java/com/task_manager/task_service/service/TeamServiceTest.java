package com.task_manager.task_service.service;

import com.task_manager.auth_service.model.entity.User;
import com.task_manager.auth_service.repository.UserRepository;
import com.task_manager.task_service.mapper.TeamMapper;
import com.task_manager.task_service.model.dto.TeamRequestDto;
import com.task_manager.task_service.model.dto.TeamResponseDto;
import com.task_manager.task_service.model.entity.Team;
import com.task_manager.task_service.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    private TeamRepository teamRepository;
    private TeamMapper teamMapper;
    private UserRepository userRepository;
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        teamRepository = mock(TeamRepository.class);
        teamMapper = mock(TeamMapper.class);
        userRepository = mock(UserRepository.class);
        teamService = new TeamService(teamRepository, teamMapper, userRepository);
    }

    @Test
    void createTeam_Success() {
        TeamRequestDto requestDto = new TeamRequestDto();
        requestDto.setCreatedBy(1L);

        User user = new User();
        user.setId(1L);

        Team team = new Team();
        team.setCreatedBy(user);

        TeamResponseDto responseDto = new TeamResponseDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(teamMapper.toModel(requestDto)).thenReturn(team);
        when(teamRepository.save(team)).thenReturn(team);
        when(teamMapper.toResponseDto(team)).thenReturn(responseDto);

        TeamResponseDto result = teamService.createTeam(requestDto);

        assertThat(result).isEqualTo(responseDto);
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    void createTeam_UserNotFound_ThrowsException() {
        TeamRequestDto requestDto = new TeamRequestDto();
        requestDto.setCreatedBy(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teamService.createTeam(requestDto));
    }

    @Test
    void findAll_ReturnsMappedList() {
        Team team1 = new Team();
        Team team2 = new Team();
        TeamResponseDto dto1 = new TeamResponseDto();
        TeamResponseDto dto2 = new TeamResponseDto();

        when(teamRepository.findAll()).thenReturn(List.of(team1, team2));
        when(teamMapper.toResponseDto(team1)).thenReturn(dto1);
        when(teamMapper.toResponseDto(team2)).thenReturn(dto2);

        List<TeamResponseDto> result = teamService.findAll();

        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    void getTeam_Success() {
        Team team = new Team();
        TeamResponseDto dto = new TeamResponseDto();

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamMapper.toResponseDto(team)).thenReturn(dto);

        TeamResponseDto result = teamService.getTeam(1L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getTeam_NotFound_ThrowsException() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.getTeam(1L));
    }

    @Test
    void update_Success() {
        TeamRequestDto requestDto = new TeamRequestDto();
        requestDto.setName("New name");
        requestDto.setDescription("New desc");
        requestDto.setCreatedBy(1L);

        User user = new User();
        user.setId(1L);

        Team team = new Team();
        TeamResponseDto responseDto = new TeamResponseDto();

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.save(team)).thenReturn(team);
        when(teamMapper.toResponseDto(team)).thenReturn(responseDto);

        TeamResponseDto result = teamService.update(1L, requestDto);

        assertThat(result).isEqualTo(responseDto);
        assertThat(team.getName()).isEqualTo("New name");
        assertThat(team.getDescription()).isEqualTo("New desc");
        assertThat(team.getCreatedBy()).isEqualTo(user);
    }

    @Test
    void update_TeamNotFound_ThrowsException() {
        TeamRequestDto requestDto = new TeamRequestDto();
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.update(1L, requestDto));
    }

    @Test
    void update_UserNotFound_ThrowsException() {
        TeamRequestDto requestDto = new TeamRequestDto();
        requestDto.setCreatedBy(1L);

        Team team = new Team();

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.update(1L, requestDto));
    }

    @Test
    void deleteById_CallsRepository() {
        teamService.deleteById(1L);
        verify(teamRepository, times(1)).deleteById(1L);
    }
}
