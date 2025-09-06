package com.task_manager.task_service.service;

import com.task_manager.auth_service.model.entity.User;
import com.task_manager.auth_service.repository.UserRepository;
import com.task_manager.task_service.mapper.TeamMapper;
import com.task_manager.task_service.model.dto.TeamRequestDto;
import com.task_manager.task_service.model.dto.TeamResponseDto;
import com.task_manager.task_service.model.entity.Team;
import com.task_manager.task_service.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;

    public TeamResponseDto createTeam(TeamRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Team team = teamMapper.toModel(requestDto);
        team.setCreatedBy(user);

        return teamMapper.toResponseDto(teamRepository.save(team));
    }

    public List<TeamResponseDto> findAll() {
        return teamRepository.findAll()
                .stream()
                .map(teamMapper::toResponseDto)
                .toList();
    }

    public TeamResponseDto getTeam(Long id) {
        return teamMapper.toResponseDto(
                teamRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Team not found"))
        );
    }

    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

    public TeamResponseDto update(Long id, TeamRequestDto requestDto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        team.setName(requestDto.getName());
        team.setDescription(requestDto.getDescription());
        User user = userRepository.findById(requestDto.getCreatedBy())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        team.setCreatedBy(user);

        return teamMapper.toResponseDto(teamRepository.save(team));
    }
}

