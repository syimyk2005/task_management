package com.task_manager.task_service.controller;

import com.task_manager.auth_service.repository.UserRepository;
import com.task_manager.task_service.model.dto.TeamMemberRequestDto;
import com.task_manager.task_service.model.dto.TeamMemberResponseDto;
import com.task_manager.task_service.model.dto.TeamRequestDto;
import com.task_manager.task_service.model.dto.TeamResponseDto;
import com.task_manager.task_service.service.TeamMemberService;
import com.task_manager.task_service.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Командалар", description = "Командалар жана катышуучулар менен иштөө API")
public class TeamController {

    private final TeamService teamService;
    private final TeamMemberService teamMemberService;
    private final UserRepository userRepository;

    @Operation(summary = "Бардык командалардын тизмесин чыгаруу", description = "Бардык командалардын тизмесин кайтарат")
    @GetMapping
    public ResponseEntity<List<TeamResponseDto>> getAll() {
        return ResponseEntity.ok(teamService.findAll());
    }

    @Operation(summary = "ID боюнча команданын тизмесин чыгаруу", description = "Команданы анын идентификатору боюнча кайтарат")
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDto> getById(
            @Parameter(description = "Команданын ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(teamService.getTeam(id));
    }

    @Operation(summary = "Жаңы команда түзүү", description = "Берилген маалыматтар менен жаңы команда түзөт")
    @PostMapping
    public ResponseEntity<TeamResponseDto> create(
            @Parameter(description = "Команданы түзүү үчүн маалыматтар") @Valid @RequestBody TeamRequestDto requestDto
    ) {
        userRepository.findById(requestDto.getCreatedBy());
        return ResponseEntity.ok(teamService.createTeam(requestDto));
    }

    @Operation(summary = "Команданы жаңыртуу", description = "Бар болгон команданы ID боюнча жаңыртат")
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDto> update(
            @Parameter(description = "Команданын ID") @PathVariable Long id,
            @Parameter(description = "Команданын жаңы маалыматтары") @RequestBody TeamRequestDto requestDto
    ) {
        return ResponseEntity.ok(teamService.update(id, requestDto));
    }

    @Operation(summary = "Команданы өчүрүү", description = "ID боюнча команданы өчүрөт")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Команданын ID") @PathVariable Long id
    ) {
        teamService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Команданын катышуучуларын алуу", description = "Белгиленген команданын катышуучуларынын тизмесин кайтарат")
    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<TeamMemberResponseDto>> getMembers(
            @Parameter(description = "Команданын ID") @PathVariable Long teamId
    ) {
        return ResponseEntity.ok(teamMemberService.getMembers(teamId));
    }

    @Operation(summary = "Командага катышуучуну кошуу", description = "Колдонуучуну команданын катышуучуларына кошот")
    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamMemberResponseDto> addMember(
            @Parameter(description = "Команданын IDси") @PathVariable Long teamId,
            @Parameter(description = "Кошула турган колдонуучунун IDси") @RequestBody TeamMemberRequestDto userId
    ) {
        return ResponseEntity.ok(teamMemberService.addMember(teamId, userId));
    }

    @Operation(summary = "Командадан катышуучуну өчүрүү", description = "Колдонуучуну команданын катышуучуларынын ичинен ID боюнча өчүрөт")
    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @Parameter(description = "Команданын ID") @PathVariable Long teamId,
            @Parameter(description = "Колдонуучунун ID") @PathVariable Long userId
    ) {
        teamMemberService.removeMember(teamId, userId);
        return ResponseEntity.noContent().build();
    }
}

