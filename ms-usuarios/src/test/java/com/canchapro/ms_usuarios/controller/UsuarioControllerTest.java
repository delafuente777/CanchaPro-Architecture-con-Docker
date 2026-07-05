package com.canchapro.ms_usuarios.controller;

import com.canchapro.ms_usuarios.exception.GlobalExceptionHandler;
import com.canchapro.ms_usuarios.exception.UsuarioNoEncontradoException;
import com.canchapro.ms_usuarios.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioControllerTest {

    private MockMvc mockMvc;
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = mock(UsuarioService.class);

        UsuarioController controller = new UsuarioController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listarTodosDebeRetornarStatusOk() throws Exception {
        when(service.listarTodos())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());

        verify(service).listarTodos();
    }

    @Test
    void buscarPorIdDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk());

        verify(service).buscarPorId(1L);
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new UsuarioNoEncontradoException("No existe usuario con ID: 99"));

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());

        verify(service).buscarPorId(99L);
    }

    @Test
    void crearDebeRetornarCreatedCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "nombre": "Caden Nieto",
                    "correo": "caden.controller@canchapro.cl",
                    "password": "password123",
                    "telefono": "987654321",
                    "role": "CLIENTE"
                }
                """;

        when(service.crear(any()))
                .thenReturn(null);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(service).crear(any());
    }

    @Test
    void crearDebeRetornarBadRequestCuandoRequestEsInvalido() throws Exception {
        String json = """
                {
                    "nombre": "",
                    "correo": "correo-malo",
                    "password": "123",
                    "telefono": "abc",
                    "role": null
                }
                """;

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarDebeRetornarStatusOkCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "nombre": "Caden Actualizado",
                    "correo": "caden.actualizado@canchapro.cl",
                    "password": "password123",
                    "telefono": "987654321",
                    "role": "CLIENTE"
                }
                """;

        when(service.actualizar(eq(1L), any()))
                .thenReturn(null);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(service).actualizar(eq(1L), any());
    }

    @Test
    void eliminarDebeRetornarNoContentCuandoExiste() throws Exception {
        doNothing()
                .when(service)
                .eliminar(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new UsuarioNoEncontradoException("No existe usuario con ID: 99"))
                .when(service)
                .eliminar(99L);

        mockMvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }

    @Test
    void desactivarDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.desactivar(1L))
                .thenReturn(null);

        mockMvc.perform(patch("/api/usuarios/1/desactivar"))
                .andExpect(status().isOk());

        verify(service).desactivar(1L);
    }

    @Test
    void desactivarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(service.desactivar(99L))
                .thenThrow(new UsuarioNoEncontradoException("No existe usuario con ID: 99"));

        mockMvc.perform(patch("/api/usuarios/99/desactivar"))
                .andExpect(status().isNotFound());

        verify(service).desactivar(99L);
    }
}