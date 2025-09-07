package com.task_manager.auth_service.controller;

import com.task_manager.auth_service.model.dto.AuthenticationResponse;
import com.task_manager.auth_service.model.dto.UserRequestDto;
import com.task_manager.auth_service.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldReturnAuthenticationResponse() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("test");
        request.setPassword("pass");

        AuthenticationResponse responseDto = new AuthenticationResponse("access-token", "refresh-token");

        when(authenticationService.register(request)).thenReturn(responseDto);

        ResponseEntity<AuthenticationResponse> response = authenticationController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void login_shouldReturnAuthenticationResponse() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("test");
        request.setPassword("pass");

        AuthenticationResponse responseDto = new AuthenticationResponse("access-token", "refresh-token");

        when(authenticationService.authenticate(request)).thenReturn(responseDto);

        ResponseEntity<AuthenticationResponse> response = authenticationController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void refreshToken_shouldDelegateToService() {
        var request = org.mockito.Mockito.mock(HttpServletRequest.class);
        var response = org.mockito.Mockito.mock(HttpServletResponse.class);

        ResponseEntity<String> serviceResponse = ResponseEntity.ok("token refreshed");
        when(authenticationService.refresh(request, response)).thenReturn(serviceResponse);

        ResponseEntity<?> actualResponse = authenticationController.refreshToken(request, response);

        assertEquals(serviceResponse, actualResponse);
    }
}
