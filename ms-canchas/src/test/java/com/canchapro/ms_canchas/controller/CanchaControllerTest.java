package com.canchapro.ms_canchas.controller;

import com.canchapro.ms_canchas.entity.EstadoCancha;
import com.canchapro.ms_canchas.entity.TipoCancha;
import com.canchapro.ms_canchas.exception.CanchaNoEncontradaException;
import com.canchapro.ms_canchas.exception.GlobalExceptionHandler;
import com.canchapro.ms_canchas.service.CanchaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CanchaControllerTest {

    private MockMvc mockMvc;
    private CanchaService service;

    @BeforeEach
    void setUp() {
        service = mock(CanchaService.class);

        CanchaController controller = new CanchaController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listarTodasDebeRetornarStatusOk() throws Exception {
        when(service.listarTodas())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/canchas"))
                .andExpect(status().isOk());

        verify(service).listarTodas();
    }

    @Test
    void buscarPorIdDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/canchas/1"))
                .andExpect(status().isOk());

        verify(service).buscarPorId(1L);
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new CanchaNoEncontradaException("No existe cancha con ID: 99"));

        mockMvc.perform(get("/api/canchas/99"))
                .andExpect(status().isNotFound());

        verify(service).buscarPorId(99L);
    }

    @Test
    void listarPorEstadoDebeRetornarStatusOk() throws Exception {
        when(service.listarPorEstado(EstadoCancha.DISPONIBLE))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/canchas/estado/DISPONIBLE"))
                .andExpect(status().isOk());

        verify(service).listarPorEstado(EstadoCancha.DISPONIBLE);
    }

    @Test
    void listarPorTipoDebeRetornarStatusOk() throws Exception {
        when(service.listarPorTipo(TipoCancha.FUTBOLITO))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/canchas/tipo/FUTBOLITO"))
                .andExpect(status().isOk());

        verify(service).listarPorTipo(TipoCancha.FUTBOLITO);
    }

    @Test
    void eliminarDebeRetornarNoContentCuandoExiste() throws Exception {
        doNothing()
                .when(service)
                .eliminar(1L);

        mockMvc.perform(delete("/api/canchas/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new CanchaNoEncontradaException("No existe cancha con ID: 99"))
                .when(service)
                .eliminar(99L);

        mockMvc.perform(delete("/api/canchas/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }

    @Test
    void cambiarEstadoDebeRetornarStatusOk() throws Exception {
        when(service.cambiarEstado(1L, EstadoCancha.DISPONIBLE))
                .thenReturn(null);

        mockMvc.perform(patch("/api/canchas/1/estado/DISPONIBLE"))
                .andExpect(status().isOk());

        verify(service).cambiarEstado(1L, EstadoCancha.DISPONIBLE);
    }

    @Test
    void validarDisponibleDebeRetornarMensajeCuandoCanchaDisponible() throws Exception {
        doNothing()
                .when(service)
                .validarCanchaDisponible(1L);

        mockMvc.perform(get("/api/canchas/1/validar-disponible"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cancha disponible"));

        verify(service).validarCanchaDisponible(1L);
    }

    @Test
    void validarDisponibleDebeRetornarNotFoundCuandoCanchaNoExiste() throws Exception {
        doThrow(new CanchaNoEncontradaException("No existe cancha con ID: 99"))
                .when(service)
                .validarCanchaDisponible(99L);

        mockMvc.perform(get("/api/canchas/99/validar-disponible"))
                .andExpect(status().isNotFound());

        verify(service).validarCanchaDisponible(99L);
    }
}