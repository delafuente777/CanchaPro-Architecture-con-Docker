package com.canchapro.ms_disponibilidad.controller;

import com.canchapro.ms_disponibilidad.entity.EstadoDisponibilidad;
import com.canchapro.ms_disponibilidad.exception.DisponibilidadNoEncontradaException;
import com.canchapro.ms_disponibilidad.exception.GlobalExceptionHandler;
import com.canchapro.ms_disponibilidad.service.DisponibilidadService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DisponibilidadControllerTest {

    private MockMvc mockMvc;
    private DisponibilidadService service;

    @BeforeEach
    void setUp() {
        service = mock(DisponibilidadService.class);

        DisponibilidadController controller =
                new DisponibilidadController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listarTodasDebeRetornarStatusOk() throws Exception {
        when(service.listarTodas())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/disponibilidad"))
                .andExpect(status().isOk());

        verify(service).listarTodas();
    }

    @Test
    void buscarPorIdDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/disponibilidad/1"))
                .andExpect(status().isOk());

        verify(service).buscarPorId(1L);
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new DisponibilidadNoEncontradaException(
                        "No existe disponibilidad con ID: 99"
                ));

        mockMvc.perform(get("/api/disponibilidad/99"))
                .andExpect(status().isNotFound());

        verify(service).buscarPorId(99L);
    }

    @Test
    void crearDebeRetornarBadRequestCuandoRequestEsInvalido() throws Exception {
        String json = """
                {
                }
                """;

        mockMvc.perform(post("/api/disponibilidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarDebeRetornarNoContentCuandoExiste() throws Exception {
        doNothing()
                .when(service)
                .eliminar(1L);

        mockMvc.perform(delete("/api/disponibilidad/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new DisponibilidadNoEncontradaException(
                "No existe disponibilidad con ID: 99"
        ))
                .when(service)
                .eliminar(99L);

        mockMvc.perform(delete("/api/disponibilidad/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }

    @Test
    void listarPorCanchaYFechaDebeRetornarStatusOk() throws Exception {
        LocalDate fecha = LocalDate.now().plusDays(1);

        when(service.listarPorCanchaYFecha(1L, fecha))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/disponibilidad/cancha/1/fecha/" + fecha))
                .andExpect(status().isOk());

        verify(service).listarPorCanchaYFecha(1L, fecha);
    }

    @Test
    void listarPorEstadoDebeRetornarStatusOk() throws Exception {
        when(service.listarPorEstado(EstadoDisponibilidad.DISPONIBLE))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/disponibilidad/estado/DISPONIBLE"))
                .andExpect(status().isOk());

        verify(service).listarPorEstado(EstadoDisponibilidad.DISPONIBLE);
    }

    @Test
    void consultarDisponibleDebeRetornarTrueCuandoHorarioDisponible() throws Exception {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);

        when(service.consultarDisponible(1L, fecha, horaInicio))
                .thenReturn(true);

        mockMvc.perform(get("/api/disponibilidad/consultar")
                        .param("canchaId", "1")
                        .param("fecha", fecha.toString())
                        .param("horaInicio", "10:00:00"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).consultarDisponible(1L, fecha, horaInicio);
    }

    @Test
    void bloquearHorarioDebeRetornarStatusOk() throws Exception {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);

        when(service.bloquearHorario(1L, fecha, horaInicio))
                .thenReturn(null);

        mockMvc.perform(patch("/api/disponibilidad/bloquear")
                        .param("canchaId", "1")
                        .param("fecha", fecha.toString())
                        .param("horaInicio", "10:00:00"))
                .andExpect(status().isOk());

        verify(service).bloquearHorario(1L, fecha, horaInicio);
    }

    @Test
    void liberarHorarioDebeRetornarStatusOk() throws Exception {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);

        when(service.liberarHorario(1L, fecha, horaInicio))
                .thenReturn(null);

        mockMvc.perform(patch("/api/disponibilidad/liberar")
                        .param("canchaId", "1")
                        .param("fecha", fecha.toString())
                        .param("horaInicio", "10:00:00"))
                .andExpect(status().isOk());

        verify(service).liberarHorario(1L, fecha, horaInicio);
    }

    @Test
    void reservarHorarioDebeRetornarStatusOk() throws Exception {
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);

        when(service.reservarHorario(1L, fecha, horaInicio))
                .thenReturn(null);

        mockMvc.perform(patch("/api/disponibilidad/reservar")
                        .param("canchaId", "1")
                        .param("fecha", fecha.toString())
                        .param("horaInicio", "10:00:00"))
                .andExpect(status().isOk());

        verify(service).reservarHorario(1L, fecha, horaInicio);
    }
}