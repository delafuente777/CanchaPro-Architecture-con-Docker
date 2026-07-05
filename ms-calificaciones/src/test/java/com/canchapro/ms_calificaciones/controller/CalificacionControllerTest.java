package com.canchapro.ms_calificaciones.controller;

import com.canchapro.ms_calificaciones.exception.CalificacionException;
import com.canchapro.ms_calificaciones.exception.CalificacionNoEncontradaException;
import com.canchapro.ms_calificaciones.exception.GlobalExceptionHandler;
import com.canchapro.ms_calificaciones.service.CalificacionService;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CalificacionControllerTest {

    private MockMvc mockMvc;
    private CalificacionService service;

    @BeforeEach
    void setUp() {
        service = mock(CalificacionService.class);

        CalificacionController controller =
                new CalificacionController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listarTodasDebeRetornarStatusOk() throws Exception {
        when(service.listarTodas())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/calificaciones"))
                .andExpect(status().isOk());

        verify(service).listarTodas();
    }

    @Test
    void buscarPorIdDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/calificaciones/1"))
                .andExpect(status().isOk());

        verify(service).buscarPorId(1L);
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new CalificacionNoEncontradaException(
                        "No existe calificacion con ID: 99"
                ));

        mockMvc.perform(get("/api/calificaciones/99"))
                .andExpect(status().isNotFound());

        verify(service).buscarPorId(99L);
    }

    @Test
    void crearDebeRetornarCreatedCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "usuarioId": 1,
                    "canchaId": 1,
                    "reservaId": 1,
                    "puntuacion": 5,
                    "comentario": "Excelente cancha"
                }
                """;

        when(service.crear(any()))
                .thenReturn(null);

        mockMvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(service).crear(any());
    }

    @Test
    void crearDebeRetornarBadRequestCuandoRequestEsInvalido() throws Exception {
        String json = """
                {
                    "usuarioId": null,
                    "canchaId": null,
                    "reservaId": null,
                    "puntuacion": 0,
                    "comentario": ""
                }
                """;

        mockMvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearDebeRetornarConflictCuandoReservaYaTieneCalificacion() throws Exception {
        String json = """
                {
                    "usuarioId": 1,
                    "canchaId": 1,
                    "reservaId": 1,
                    "puntuacion": 5,
                    "comentario": "Excelente cancha"
                }
                """;

        when(service.crear(any()))
                .thenThrow(new CalificacionException(
                        "La reserva ya tiene una calificacion registrada"
                ));

        mockMvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        verify(service).crear(any());
    }

    @Test
    void actualizarDebeRetornarStatusOkCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "usuarioId": 1,
                    "canchaId": 1,
                    "reservaId": 1,
                    "puntuacion": 4,
                    "comentario": "Buena cancha"
                }
                """;

        when(service.actualizar(eq(1L), any()))
                .thenReturn(null);

        mockMvc.perform(put("/api/calificaciones/1")
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

        mockMvc.perform(delete("/api/calificaciones/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new CalificacionNoEncontradaException(
                "No existe calificacion con ID: 99"
        ))
                .when(service)
                .eliminar(99L);

        mockMvc.perform(delete("/api/calificaciones/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }

    @Test
    void listarPorUsuarioDebeRetornarStatusOk() throws Exception {
        when(service.listarPorUsuario(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/calificaciones/usuario/1"))
                .andExpect(status().isOk());

        verify(service).listarPorUsuario(1L);
    }

    @Test
    void listarPorCanchaDebeRetornarStatusOk() throws Exception {
        when(service.listarPorCancha(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/calificaciones/cancha/1"))
                .andExpect(status().isOk());

        verify(service).listarPorCancha(1L);
    }

    @Test
    void buscarPorReservaDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.buscarPorReserva(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/calificaciones/reserva/1"))
                .andExpect(status().isOk());

        verify(service).buscarPorReserva(1L);
    }

    @Test
    void buscarPorReservaDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(service.buscarPorReserva(99L))
                .thenThrow(new CalificacionNoEncontradaException(
                        "No existe calificacion para la reserva"
                ));

        mockMvc.perform(get("/api/calificaciones/reserva/99"))
                .andExpect(status().isNotFound());

        verify(service).buscarPorReserva(99L);
    }

    @Test
    void obtenerResumenPorCanchaDebeRetornarStatusOk() throws Exception {
        when(service.obtenerResumenPorCancha(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/calificaciones/cancha/1/resumen"))
                .andExpect(status().isOk());

        verify(service).obtenerResumenPorCancha(1L);
    }
}