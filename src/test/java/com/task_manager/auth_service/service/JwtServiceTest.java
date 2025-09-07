package com.task_manager.auth_service.service;

import com.task_manager.auth_service.model.entity.Token;
import com.task_manager.auth_service.model.entity.User;
import com.task_manager.auth_service.repository.TokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private TokenRepository tokenRepository;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(TokenRepository.class);
        jwtService = new JwtService(tokenRepository);
        try {
            var field = JwtService.class.getDeclaredField("secretKey");
            field.setAccessible(true);
            String SECRET_KEY = "12345678901234567890123456789012";
            field.set(jwtService, SECRET_KEY);
            var accessField = JwtService.class.getDeclaredField("accessTokenExpire");
            accessField.setAccessible(true);
            accessField.set(jwtService, 1000L * 60 * 60);
            var refreshField = JwtService.class.getDeclaredField("refreshTokenExpire");
            refreshField.setAccessible(true);
            refreshField.set(jwtService, 1000L * 60 * 60 * 24);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGenerateAndValidateAccessToken() {
        User user = new User();
        user.setUsername("testuser");
        String token = jwtService.generateAccessToken(user);
        assertThat(token).isNotNull();
        String username = jwtService.extractUsername(token);
        assertThat(username).isEqualTo("testuser");
        assertThat(jwtService.isTokenExpired(token)).isFalse();
        Token dbToken = new Token();
        dbToken.setToken(token);
        dbToken.setRevoked(false);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(dbToken));
        boolean isValid = jwtService.isValid(token, user);
        assertThat(isValid).isTrue();
        verify(tokenRepository, times(1)).findByToken(token);
    }

    @Test
    void testTokenExpired() {
        User user = new User();
        user.setUsername("testuser");
        String token = jwtService.generateToken(user, -1000);
        Assertions.assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            jwtService.isTokenExpired(token);
        });
    }

    @Test
    void testIsValidRefreshToken() {
        User user = new User();
        user.setUsername("testuser");
        String token = jwtService.generateRefreshToken(user);
        boolean valid = jwtService.isValidRefreshToken(token, user);
        assertThat(valid).isTrue();
    }

    @Test
    void testGetSignKey() {
        SecretKey key = jwtService.getSignKey();
        assertThat(key).isNotNull();
        assertThat(key.getAlgorithm()).isEqualTo("HmacSHA256");
    }
}
