package com.canchapro.ms_notificaciones.controller;

import com.canchapro.ms_notificaciones.entity.EstadoNotificacion;
import com.canchapro.ms_notificaciones.entity.TipoNotificacion;
import com.canchapro.ms_notificaciones.exception.GlobalExceptionHandler;
import com.canchapro.ms_notificaciones.exception.NotificacionException;
import com.canchapro.ms_notificaciones.exception.NotificacionNoEncontradaException;
import com.canchapro.ms_notificaciones.service.NotificacionService;

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

class NotificacionControllerTest {

    private MockMvc mockMvc;
    private NotificacionService service;

    @BeforeEach
    void setUp() {
        service = mock(NotificacionService.class);

        NotificacionController controller =
                new NotificacionController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listarTodasDebeRetornarStatusOk() throws Exception {
        when(service.listarTodas())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk());

        verify(service).listarTodas();
    }

    @Test
    void buscarPorIdDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/notificaciones/1"))
                .andExpect(status().isOk());

        verify(service).buscarPorId(1L);
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new NotificacionNoEncontradaException(
                        "No existe notificacion con ID: 99"
                ));

        mockMvc.perform(get("/api/notificaciones/99"))
                .andExpect(status().isNotFound());

        verify(service).buscarPorId(99L);
    }

    @Test
    void crearDebeRetornarCreatedCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "usuarioId": 1,
                    "reservaId": 1,
                    "titulo": "Reserva creada",
                    "mensaje": "Tu reserva fue creada correctamente",
                    "tipo": "RESERVA"
                }
                """;

        when(service.crear(any()))
                .thenReturn(null);

        mockMvc.perform(post("/api/notificaciones")
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
                    "reservaId": null,
                    "titulo": "",
                    "mensaje": "",
                    "tipo": null
                }
                """;

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarDebeRetornarStatusOkCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "usuarioId": 1,
                    "reservaId": 1,
                    "titulo": "Notificacion actualizada",
                    "mensaje": "Mensaje actualizado correctamente",
                    "tipo": "SISTEMA"
                }
                """;

        when(service.actualizar(eq(1L), any()))
                .thenReturn(null);

        mockMvc.perform(put("/api/notificaciones/1")
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

        mockMvc.perform(delete("/api/notificaciones/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new NotificacionNoEncontradaException(
                "No existe notificacion con ID: 99"
        ))
                .when(service)
                .eliminar(99L);

        mockMvc.perform(delete("/api/notificaciones/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }

    @Test
    void marcarEnviadaDebeRetornarStatusOk() throws Exception {
        when(service.marcarEnviada(1L))
                .thenReturn(null);

        mockMvc.perform(patch("/api/notificaciones/1/enviar"))
                .andExpect(status().isOk());

        verify(service).marcarEnviada(1L);
    }

    @Test
    void marcarLeidaDebeRetornarStatusOk() throws Exception {
        when(service.marcarLeida(1L))
                .thenReturn(null);

        mockMvc.perform(patch("/api/notificaciones/1/leer"))
                .andExpect(status().isOk());

        verify(service).marcarLeida(1L);
    }

    @Test
    void marcarFallidaDebeRetornarStatusOk() throws Exception {
        when(service.marcarFallida(1L))
                .thenReturn(null);

        mockMvc.perform(patch("/api/notificaciones/1/fallar"))
                .andExpect(status().isOk());

        verify(service).marcarFallida(1L);
    }

    @Test
    void marcarFallidaDebeRetornarConflictCuandoNoSePuedeCambiar() throws Exception {
        when(service.marcarFallida(1L))
                .thenThrow(new NotificacionException(
                        "No se puede marcar como fallida"
                ));

        mockMvc.perform(patch("/api/notificaciones/1/fallar"))
                .andExpect(status().isConflict());

        verify(service).marcarFallida(1L);
    }

    @Test
    void cambiarEstadoDebeRetornarStatusOkCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "estado": "ENVIADA"
                }
                """;

        when(service.cambiarEstado(eq(1L), any()))
                .thenReturn(null);

        mockMvc.perform(patch("/api/notificaciones/1/estado")
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

        mockMvc.perform(patch("/api/notificaciones/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarPorUsuarioDebeRetornarStatusOk() throws Exception {
        when(service.listarPorUsuario(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/notificaciones/usuario/1"))
                .andExpect(status().isOk());

        verify(service).listarPorUsuario(1L);
    }

    @Test
    void listarPorReservaDebeRetornarStatusOk() throws Exception {
        when(service.listarPorReserva(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/notificaciones/reserva/1"))
                .andExpect(status().isOk());

        verify(service).listarPorReserva(1L);
    }

    @Test
    void listarPorEstadoDebeRetornarStatusOk() throws Exception {
        when(service.listarPorEstado(EstadoNotificacion.PENDIENTE))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/notificaciones/estado/PENDIENTE"))
                .andExpect(status().isOk());

        verify(service).listarPorEstado(EstadoNotificacion.PENDIENTE);
    }

    @Test
    void listarPorTipoDebeRetornarStatusOk() throws Exception {
        when(service.listarPorTipo(TipoNotificacion.RESERVA))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/notificaciones/tipo/RESERVA"))
                .andExpect(status().isOk());

        verify(service).listarPorTipo(TipoNotificacion.RESERVA);
    }

    @Test
    void listarPorUsuarioYEstadoDebeRetornarStatusOk() throws Exception {
        when(service.listarPorUsuarioYEstado(1L, EstadoNotificacion.ENVIADA))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/notificaciones/usuario/1/estado/ENVIADA"))
                .andExpect(status().isOk());

        verify(service).listarPorUsuarioYEstado(
                1L,
                EstadoNotificacion.ENVIADA
        );
    }
}