package com.task_manager.auth_service.controller;

import com.task_manager.auth_service.model.dto.UserRequestDto;
import com.task_manager.auth_service.model.dto.UserResponseDto;
import com.task_manager.auth_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Колдунуучуларды башкаруу. (CRUD операциялары)")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Колдонуучулардын тизмесин кайтаруу", description = "Системадагы ар бир колдонуучусун кайтарат.")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> allUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Id боюнча колдонуучуну кайтаруу", description = "Колдонуучуну жеке номери боюнча кайтарат.")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Колдонуучуну жаныртуу.", description = "Id боюнча конлодонуучуну жаныртат.")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(summary = "Колдонуучуну базадан өчүрүү.", description = "Id боюнча колдонуучуну базадан чыгарат.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
