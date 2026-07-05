package com.canchapro.auth_service.service;

import com.canchapro.auth_service.dto.AuthResponseDTO;
import com.canchapro.auth_service.dto.LoginRequestDTO;
import com.canchapro.auth_service.dto.RegisterRequestDTO;
import com.canchapro.auth_service.entity.Role;
import com.canchapro.auth_service.entity.User;
import com.canchapro.auth_service.exception.InvalidCredentialsException;
import com.canchapro.auth_service.exception.UserAlreadyExistsException;
import com.canchapro.auth_service.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService service;

    @Test
    void registerDebeRegistrarUsuarioCuandoCorreoNoExiste() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Caden Nieto",
                "caden.auth@canchapro.cl",
                "password123",
                "987654321",
                Role.CLIENTE
        );

        when(repository.existsByCorreo("caden.auth@canchapro.cl"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("password-codificada");

        when(jwtService.generateToken(any(User.class)))
                .thenReturn("jwt-token-test");

        AuthResponseDTO respuesta = service.register(request);

        assertNotNull(respuesta);
        assertEquals("jwt-token-test", respuesta.getToken());
        assertEquals("Usuario registrado correctamente", respuesta.getMensaje());

        verify(repository).existsByCorreo("caden.auth@canchapro.cl");
        verify(passwordEncoder).encode("password123");
        verify(repository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void registerDebeLanzarExcepcionCuandoCorreoYaExiste() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Caden Nieto",
                "caden.auth@canchapro.cl",
                "password123",
                "987654321",
                Role.CLIENTE
        );

        when(repository.existsByCorreo("caden.auth@canchapro.cl"))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> service.register(request)
        );

        verify(repository).existsByCorreo("caden.auth@canchapro.cl");
        verify(passwordEncoder, never()).encode("password123");
        verify(repository, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    void loginDebeRetornarTokenCuandoCredencialesSonValidas() {
        LoginRequestDTO request = new LoginRequestDTO(
                "caden.auth@canchapro.cl",
                "password123"
        );

        User user = User.builder()
                .id(1L)
                .nombre("Caden Nieto")
                .correo("caden.auth@canchapro.cl")
                .password("password-codificada")
                .telefono("987654321")
                .role(Role.CLIENTE)
                .build();

        when(repository.findByCorreo("caden.auth@canchapro.cl"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password123", "password-codificada"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token-login");

        AuthResponseDTO respuesta = service.login(request);

        assertNotNull(respuesta);
        assertEquals("jwt-token-login", respuesta.getToken());
        assertEquals("Login exitoso", respuesta.getMensaje());

        verify(repository).findByCorreo("caden.auth@canchapro.cl");
        verify(passwordEncoder).matches("password123", "password-codificada");
        verify(jwtService).generateToken(user);
    }

    @Test
    void loginDebeLanzarExcepcionCuandoCorreoNoExiste() {
        LoginRequestDTO request = new LoginRequestDTO(
                "noexiste@canchapro.cl",
                "password123"
        );

        when(repository.findByCorreo("noexiste@canchapro.cl"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> service.login(request)
        );

        verify(repository).findByCorreo("noexiste@canchapro.cl");
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    void loginDebeLanzarExcepcionCuandoPasswordEsIncorrecta() {
        LoginRequestDTO request = new LoginRequestDTO(
                "caden.auth@canchapro.cl",
                "password-mala"
        );

        User user = User.builder()
                .id(1L)
                .nombre("Caden Nieto")
                .correo("caden.auth@canchapro.cl")
                .password("password-codificada")
                .telefono("987654321")
                .role(Role.CLIENTE)
                .build();

        when(repository.findByCorreo("caden.auth@canchapro.cl"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password-mala", "password-codificada"))
                .thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> service.login(request)
        );

        verify(repository).findByCorreo("caden.auth@canchapro.cl");
        verify(passwordEncoder).matches("password-mala", "password-codificada");
        verify(jwtService, never()).generateToken(any(User.class));
    }
}