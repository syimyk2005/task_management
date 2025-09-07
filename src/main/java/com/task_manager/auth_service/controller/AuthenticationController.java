package com.task_manager.auth_service.controller;

import com.task_manager.auth_service.model.dto.AuthenticationResponse;
import com.task_manager.auth_service.model.dto.UserRequestDto;
import com.task_manager.auth_service.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Регистрация, логин и обновление токена")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Жаны колдонуучуну каттоо.", description = "Жаны колдонуучуну жаратат, жана Access, Refresh токендерин кайтарат.")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody UserRequestDto request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(summary = "Колдонуучулардын кируу эндпоинти", description = "Аутентифиция кылып токендерди кайтарат")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserRequestDto request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @Operation(summary = "Токенди жаныртуу эндпоинти", description = "Refresh token менен access токенди жаныртат")
    @PostMapping("/refresh_token")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return authenticationService.refresh(request, response);
    }
}
