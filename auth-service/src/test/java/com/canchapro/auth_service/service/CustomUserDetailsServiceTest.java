package com.canchapro.auth_service.service;

import com.canchapro.auth_service.entity.Role;
import com.canchapro.auth_service.entity.User;
import com.canchapro.auth_service.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    void loadUserByUsernameDebeRetornarUsuarioCuandoExisteCorreo() {
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

        UserDetails resultado = service.loadUserByUsername("caden.auth@canchapro.cl");

        assertEquals("caden.auth@canchapro.cl", resultado.getUsername());
        assertEquals("password-codificada", resultado.getPassword());
        assertTrue(resultado.isEnabled());
        assertTrue(resultado.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CLIENTE")));

        verify(repository).findByCorreo("caden.auth@canchapro.cl");
    }

    @Test
    void loadUserByUsernameDebeLanzarExcepcionCuandoCorreoNoExiste() {
        when(repository.findByCorreo("noexiste@canchapro.cl"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("noexiste@canchapro.cl")
        );

        verify(repository).findByCorreo("noexiste@canchapro.cl");
    }
}