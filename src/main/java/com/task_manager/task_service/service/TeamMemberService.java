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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberMapper teamMemberMapper;
    private final UserRepository userRepository;

    public List<TeamMemberResponseDto> getMembers(Long teamId) {
        return teamMemberRepository.findAll()
                .stream()
                .map(teamMemberMapper::toResponseDto)
                .toList();
    }

    public TeamMemberResponseDto addMember(Long teamId, TeamMemberRequestDto requestDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        TeamMember teamMember = new TeamMember();
        teamMember.setTeam(team);
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found."));
        teamMember.setUser(user);
        teamMember.setJoinedAt(Instant.now());

        return teamMemberMapper.toResponseDto(teamMemberRepository.save(teamMember));
    }

    @Transactional
    public void removeMember(Long teamId, Long userId) {
        teamMemberRepository.deleteByTeamIdAndUserId(teamId, userId);
    }
}
