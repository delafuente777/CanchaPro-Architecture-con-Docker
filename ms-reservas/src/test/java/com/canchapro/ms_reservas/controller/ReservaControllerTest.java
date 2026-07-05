package com.canchapro.ms_reservas.controller;

import com.canchapro.ms_reservas.entity.EstadoReserva;
import com.canchapro.ms_reservas.exception.GlobalExceptionHandler;
import com.canchapro.ms_reservas.exception.ReservaException;
import com.canchapro.ms_reservas.exception.ReservaNoEncontradaException;
import com.canchapro.ms_reservas.service.ReservaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
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

class ReservaControllerTest {

    private MockMvc mockMvc;
    private ReservaService service;

    @BeforeEach
    void setUp() {
        service = mock(ReservaService.class);

        ReservaController controller = new ReservaController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listarTodasDebeRetornarStatusOk() throws Exception {
        when(service.listarTodas())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk());

        verify(service).listarTodas();
    }

    @Test
    void buscarPorIdDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/reservas/1"))
                .andExpect(status().isOk());

        verify(service).buscarPorId(1L);
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new ReservaNoEncontradaException(
                        "No existe reserva con ID: 99"
                ));

        mockMvc.perform(get("/api/reservas/99"))
                .andExpect(status().isNotFound());

        verify(service).buscarPorId(99L);
    }

    @Test
    void crearDebeRetornarCreatedCuandoRequestEsValido() throws Exception {
        String fecha = LocalDate.now().plusDays(1).toString();

        String json = """
                {
                    "usuarioId": 1,
                    "canchaId": 1,
                    "fecha": "%s",
                    "horaInicio": "10:00:00",
                    "horaFin": "11:00:00",
                    "monto": 25000
                }
                """.formatted(fecha);

        when(service.crear(any()))
                .thenReturn(null);

        mockMvc.perform(post("/api/reservas")
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
                    "fecha": null,
                    "horaInicio": null,
                    "horaFin": null,
                    "monto": -1
                }
                """;

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarDebeRetornarStatusOkCuandoRequestEsValido() throws Exception {
        String fecha = LocalDate.now().plusDays(1).toString();

        String json = """
                {
                    "usuarioId": 1,
                    "canchaId": 1,
                    "fecha": "%s",
                    "horaInicio": "10:00:00",
                    "horaFin": "11:00:00",
                    "monto": 25000
                }
                """.formatted(fecha);

        when(service.actualizar(eq(1L), any()))
                .thenReturn(null);

        mockMvc.perform(put("/api/reservas/1")
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

        mockMvc.perform(delete("/api/reservas/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new ReservaNoEncontradaException("No existe reserva con ID: 99"))
                .when(service)
                .eliminar(99L);

        mockMvc.perform(delete("/api/reservas/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }

    @Test
    void cancelarDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.cancelar(1L))
                .thenReturn(null);

        mockMvc.perform(patch("/api/reservas/1/cancelar"))
                .andExpect(status().isOk());

        verify(service).cancelar(1L);
    }

    @Test
    void cancelarDebeRetornarConflictCuandoReservaNoSePuedeCancelar() throws Exception {
        when(service.cancelar(1L))
                .thenThrow(new ReservaException("La reserva no se puede cancelar"));

        mockMvc.perform(patch("/api/reservas/1/cancelar"))
                .andExpect(status().isConflict());

        verify(service).cancelar(1L);
    }

    @Test
    void cambiarEstadoDebeRetornarStatusOkCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "estado": "CONFIRMADA"
                }
                """;

        when(service.cambiarEstado(eq(1L), any()))
                .thenReturn(null);

        mockMvc.perform(patch("/api/reservas/1/estado")
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

        mockMvc.perform(patch("/api/reservas/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void confirmarPagoDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.confirmarPago(1L))
                .thenReturn(null);

        mockMvc.perform(patch("/api/reservas/1/confirmar-pago"))
                .andExpect(status().isOk());

        verify(service).confirmarPago(1L);
    }

    @Test
    void listarPorUsuarioDebeRetornarStatusOk() throws Exception {
        when(service.listarPorUsuario(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/reservas/usuario/1"))
                .andExpect(status().isOk());

        verify(service).listarPorUsuario(1L);
    }

    @Test
    void listarPorCanchaDebeRetornarStatusOk() throws Exception {
        when(service.listarPorCancha(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/reservas/cancha/1"))
                .andExpect(status().isOk());

        verify(service).listarPorCancha(1L);
    }

    @Test
    void listarPorEstadoDebeRetornarStatusOk() throws Exception {
        when(service.listarPorEstado(EstadoReserva.PENDIENTE))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/reservas/estado/PENDIENTE"))
                .andExpect(status().isOk());

        verify(service).listarPorEstado(EstadoReserva.PENDIENTE);
    }

    @Test
    void listarPorCanchaYFechaDebeRetornarStatusOk() throws Exception {
        LocalDate fecha = LocalDate.now().plusDays(1);

        when(service.listarPorCanchaYFecha(1L, fecha))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/reservas/cancha/1/fecha/" + fecha))
                .andExpect(status().isOk());

        verify(service).listarPorCanchaYFecha(1L, fecha);
    }
}