package com.task_manager.task_service.service;

import com.task_manager.auth_service.exception.UserNotFoundException;
import com.task_manager.auth_service.model.entity.User;
import com.task_manager.auth_service.repository.UserRepository;
import com.task_manager.task_service.mapper.TeamMemberMapper;
import com.task_manager.task_service.model.dto.TeamMemberRequestDto;
import com.task_manager.task_service.model.dto.TeamMemberResponseDto;
import com.task_manager.task_service.model.entity.Team;
import com.task_manager.task_service.model.entity.TeamMember;
import com.task_manager.task_service.repository.TeamMemberRepository;
import com.task_manager.task_service.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TeamMemberServiceTest {

    private TeamMemberRepository teamMemberRepository;
    private TeamRepository teamRepository;
    private TeamMemberMapper teamMemberMapper;
    private UserRepository userRepository;
    private TeamMemberService teamMemberService;

    @BeforeEach
    void setUp() {
        teamMemberRepository = mock(TeamMemberRepository.class);
        teamRepository = mock(TeamRepository.class);
        teamMemberMapper = mock(TeamMemberMapper.class);
        userRepository = mock(UserRepository.class);
        teamMemberService = new TeamMemberService(teamMemberRepository, teamRepository, teamMemberMapper, userRepository);
    }

    @Test
    void getMembers_ReturnsMappedList() {
        TeamMember member1 = new TeamMember();
        TeamMember member2 = new TeamMember();
        TeamMemberResponseDto dto1 = new TeamMemberResponseDto();
        TeamMemberResponseDto dto2 = new TeamMemberResponseDto();

        when(teamMemberRepository.findAll()).thenReturn(List.of(member1, member2));
        when(teamMemberMapper.toResponseDto(member1)).thenReturn(dto1);
        when(teamMemberMapper.toResponseDto(member2)).thenReturn(dto2);

        List<TeamMemberResponseDto> result = teamMemberService.getMembers(1L);

        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    void addMember_Success() {
        Long teamId = 1L;
        Team team = new Team();
        User user = new User();
        user.setId(2L);
        TeamMemberRequestDto requestDto = new TeamMemberRequestDto();
        requestDto.setUserId(2L);

        TeamMember teamMember = new TeamMember();
        teamMember.setTeam(team);
        teamMember.setUser(user);
        teamMember.setJoinedAt(Instant.now());

        TeamMemberResponseDto responseDto = new TeamMemberResponseDto();

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(teamMemberRepository.save(any(TeamMember.class))).thenReturn(teamMember);
        when(teamMemberMapper.toResponseDto(teamMember)).thenReturn(responseDto);

        TeamMemberResponseDto result = teamMemberService.addMember(teamId, requestDto);

        assertThat(result).isEqualTo(responseDto);
        verify(teamMemberRepository, times(1)).save(any(TeamMember.class));
    }

    @Test
    void addMember_UserNotFound_ThrowsException() {
        Long teamId = 1L;
        Team team = new Team();
        TeamMemberRequestDto requestDto = new TeamMemberRequestDto();
        requestDto.setUserId(2L);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> teamMemberService.addMember(teamId, requestDto));
    }

    @Test
    void addMember_TeamNotFound_ThrowsException() {
        Long teamId = 1L;
        TeamMemberRequestDto requestDto = new TeamMemberRequestDto();
        requestDto.setUserId(2L);

        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teamMemberService.addMember(teamId, requestDto));
    }

    @Test
    void removeMember_CallsRepository() {
        Long teamId = 1L;
        Long userId = 2L;

        teamMemberService.removeMember(teamId, userId);

        verify(teamMemberRepository, times(1)).deleteByTeamIdAndUserId(teamId, userId);
    }
}
