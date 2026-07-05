package com.canchapro.ms_pagos.controller;

import com.canchapro.ms_pagos.exception.GlobalExceptionHandler;
import com.canchapro.ms_pagos.exception.PagoException;
import com.canchapro.ms_pagos.exception.PagoNoEncontradoException;
import com.canchapro.ms_pagos.service.PagoService;

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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PagoControllerTest {

    private MockMvc mockMvc;
    private PagoService service;

    @BeforeEach
    void setUp() {
        service = mock(PagoService.class);

        PagoController controller = new PagoController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listarTodosDebeRetornarStatusOk() throws Exception {
        when(service.listarTodos())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk());

        verify(service).listarTodos();
    }

    @Test
    void buscarPorIdDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/pagos/1"))
                .andExpect(status().isOk());

        verify(service).buscarPorId(1L);
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new PagoNoEncontradoException("No existe pago con ID: 99"));

        mockMvc.perform(get("/api/pagos/99"))
                .andExpect(status().isNotFound());

        verify(service).buscarPorId(99L);
    }

    @Test
    void registrarPagoDebeRetornarCreatedCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "reservaId": 1,
                    "monto": 25000,
                    "metodoPago": "DEBITO"
                }
                """;

        when(service.registrarPago(any()))
                .thenReturn(null);

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(service).registrarPago(any());
    }

    @Test
    void registrarPagoDebeRetornarBadRequestCuandoRequestEsInvalido() throws Exception {
        String json = """
                {
                    "reservaId": null,
                    "monto": -1,
                    "metodoPago": null
                }
                """;

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void historialPorReservaDebeRetornarStatusOk() throws Exception {
        when(service.historialPorReserva(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/pagos/reserva/1"))
                .andExpect(status().isOk());

        verify(service).historialPorReserva(1L);
    }

    @Test
    void validarPagoDebeRetornarStatusOkCuandoExistePagoAprobado() throws Exception {
        when(service.validarPago(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/pagos/validar/1"))
                .andExpect(status().isOk());

        verify(service).validarPago(1L);
    }

    @Test
    void validarPagoDebeRetornarConflictCuandoNoHayPagoAprobado() throws Exception {
        when(service.validarPago(99L))
                .thenThrow(new PagoException("No existe pago aprobado para la reserva"));

        mockMvc.perform(get("/api/pagos/validar/99"))
                .andExpect(status().isConflict());

        verify(service).validarPago(99L);
    }

    @Test
    void cambiarEstadoDebeRetornarStatusOkCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "estado": "APROBADO"
                }
                """;

        when(service.cambiarEstado(eq(1L), any()))
                .thenReturn(null);

        mockMvc.perform(patch("/api/pagos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(service).cambiarEstado(eq(1L), any());
    }

    @Test
    void cambiarEstadoDebeRetornarBadRequestCuandoRequestEsInvalido() throws Exception {
        String json = """
                {
                    "estado": null
                }
                """;

        mockMvc.perform(patch("/api/pagos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarDebeRetornarNoContentCuandoExiste() throws Exception {
        doNothing()
                .when(service)
                .eliminar(1L);

        mockMvc.perform(delete("/api/pagos/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new PagoNoEncontradoException("No existe pago con ID: 99"))
                .when(service)
                .eliminar(99L);

        mockMvc.perform(delete("/api/pagos/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }
}