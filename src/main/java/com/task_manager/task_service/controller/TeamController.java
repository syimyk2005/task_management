package com.task_manager.task_service.controller;

import com.task_manager.auth_service.model.entity.User;
import com.task_manager.auth_service.repository.UserRepository;
import com.task_manager.task_service.model.dto.TeamMemberRequestDto;
import com.task_manager.task_service.model.dto.TeamMemberResponseDto;
import com.task_manager.task_service.model.dto.TeamRequestDto;
import com.task_manager.task_service.model.dto.TeamResponseDto;
import com.task_manager.task_service.model.entity.Team;
import com.task_manager.task_service.model.entity.TeamMember;
import com.task_manager.task_service.service.TeamMemberService;
import com.task_manager.task_service.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamMemberService teamMemberService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<TeamResponseDto>> getAll() {
        return ResponseEntity.ok(teamService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeam(id));
    }

    @PostMapping
    public ResponseEntity<TeamResponseDto> create(@RequestBody TeamRequestDto requestDto) {
        userRepository.findById(requestDto.getCreatedBy());
        return ResponseEntity.ok(teamService.createTeam(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDto> update(@PathVariable Long id, @RequestBody TeamRequestDto requestDto) {
        return ResponseEntity.ok(teamService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teamService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<TeamMemberResponseDto>> getMembers(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamMemberService.getMembers(teamId));
    }

    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamMemberResponseDto> addMember(@PathVariable Long teamId, @RequestBody TeamMemberRequestDto userId) {
        return ResponseEntity.ok(teamMemberService.addMember(teamId, userId));
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long teamId, @PathVariable Long userId) {
        teamMemberService.removeMember(teamId, userId);
        return ResponseEntity.noContent().build();
    }
}
