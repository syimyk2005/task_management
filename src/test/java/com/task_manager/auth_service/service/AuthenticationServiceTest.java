package com.task_manager.auth_service.service;

import com.task_manager.auth_service.mapper.UserMapper;
import com.task_manager.auth_service.model.dto.AuthenticationResponse;
import com.task_manager.auth_service.model.dto.UserRequestDto;
import com.task_manager.auth_service.model.entity.Token;
import com.task_manager.auth_service.model.entity.User;
import com.task_manager.auth_service.repository.TokenRepository;
import com.task_manager.auth_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldReturnTokens() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("testuser");
        request.setPassword("password");

        User user = new User();
        user.setId(1L);
        when(userMapper.toModel(request)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void authenticate_shouldReturnTokens() {
        UserRequestDto request = new UserRequestDto();
        request.setUsername("testuser");
        request.setPassword("password");

        User user = new User();
        user.setId(1L);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void revokeAllTokenByUser_shouldMarkTokensAsRevoked() {
        User user = new User();
        user.setId(1L);

        Token token = new Token();
        token.setRevoked(false);

        List<Token> tokens = new ArrayList<>();
        tokens.add(token);

        when(tokenRepository.findAllTokenByUser(1L)).thenReturn(tokens);

        authenticationService.revokeAllTokenByUser(user);

        assertTrue(token.isRevoked());
    }

    @Test
    void saveUserToken_shouldSaveToken() {
        User user = new User();
        user.setId(1L);

        when(jwtService.extractExpiration("token")).thenReturn(new Date(System.currentTimeMillis()));

        authenticationService.saveUserToken("token", user);

        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void refresh_shouldReturnUnauthorizedIfHeaderMissing() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        assertEquals(401, authenticationService.refresh(request, response).getStatusCodeValue());
    }
}
