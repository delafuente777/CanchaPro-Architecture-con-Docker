package com.canchapro.ms_usuarios.service;

import com.canchapro.ms_usuarios.dto.UsuarioRequestDTO;
import com.canchapro.ms_usuarios.dto.UsuarioResponseDTO;
import com.canchapro.ms_usuarios.entity.Role;
import com.canchapro.ms_usuarios.entity.Usuario;
import com.canchapro.ms_usuarios.exception.UsuarioNoEncontradoException;
import com.canchapro.ms_usuarios.exception.UsuarioYaExisteException;
import com.canchapro.ms_usuarios.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    @Test
    void crearDebeGuardarUsuarioCuandoCorreoNoExiste() {
        UsuarioRequestDTO request = new UsuarioRequestDTO(
                "Caden Nieto",
                "caden.test@canchapro.cl",
                "123456",
                Role.CLIENTE,
                "987654321"
        );

        Usuario usuarioGuardado = Usuario.builder()
                .id(1L)
                .nombre("Caden Nieto")
                .correo("caden.test@canchapro.cl")
                .password("password-codificada")
                .role(Role.CLIENTE)
                .telefono("987654321")
                .activo(true)
                .build();

        when(repository.existsByCorreo("caden.test@canchapro.cl"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("password-codificada");

        when(repository.save(any(Usuario.class)))
                .thenReturn(usuarioGuardado);

        UsuarioResponseDTO respuesta = service.crear(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Caden Nieto", respuesta.getNombre());
        assertEquals("caden.test@canchapro.cl", respuesta.getCorreo());
        assertEquals(Role.CLIENTE, respuesta.getRole());
        assertEquals("987654321", respuesta.getTelefono());
        assertEquals(true, respuesta.getActivo());

        verify(repository).existsByCorreo("caden.test@canchapro.cl");
        verify(passwordEncoder).encode("123456");
        verify(repository).save(any(Usuario.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoCorreoYaExiste() {
        UsuarioRequestDTO request = new UsuarioRequestDTO(
                "Caden Nieto",
                "caden.test@canchapro.cl",
                "123456",
                Role.CLIENTE,
                "987654321"
        );

        when(repository.existsByCorreo("caden.test@canchapro.cl"))
                .thenReturn(true);

        assertThrows(
                UsuarioYaExisteException.class,
                () -> service.crear(request)
        );

        verify(repository).existsByCorreo("caden.test@canchapro.cl");
        verify(repository, never()).save(any(Usuario.class));
        verify(passwordEncoder, never()).encode("123456");
    }

    @Test
    void buscarPorIdDebeRetornarUsuarioCuandoExiste() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Caden Nieto")
                .correo("caden.test@canchapro.cl")
                .password("password-codificada")
                .role(Role.CLIENTE)
                .telefono("987654321")
                .activo(true)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(usuario));

        UsuarioResponseDTO respuesta = service.buscarPorId(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Caden Nieto", respuesta.getNombre());
        assertEquals("caden.test@canchapro.cl", respuesta.getCorreo());
        assertEquals(Role.CLIENTE, respuesta.getRole());
        assertEquals(true, respuesta.getActivo());

        verify(repository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                UsuarioNoEncontradoException.class,
                () -> service.buscarPorId(99L)
        );

        verify(repository).findById(99L);
    }

    @Test
    void desactivarDebeCambiarActivoAFalse() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Caden Nieto")
                .correo("caden.test@canchapro.cl")
                .password("password-codificada")
                .role(Role.CLIENTE)
                .telefono("987654321")
                .activo(true)
                .build();

        Usuario usuarioDesactivado = Usuario.builder()
                .id(1L)
                .nombre("Caden Nieto")
                .correo("caden.test@canchapro.cl")
                .password("password-codificada")
                .role(Role.CLIENTE)
                .telefono("987654321")
                .activo(false)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(repository.save(any(Usuario.class)))
                .thenReturn(usuarioDesactivado);

        UsuarioResponseDTO respuesta = service.desactivar(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertFalse(respuesta.getActivo());

        verify(repository).findById(1L);
        verify(repository).save(any(Usuario.class));
    }
}