package com.canchapro.ms_reportes.controller;

import com.canchapro.ms_reportes.entity.EstadoReporte;
import com.canchapro.ms_reportes.entity.TipoReporte;
import com.canchapro.ms_reportes.exception.GlobalExceptionHandler;
import com.canchapro.ms_reportes.exception.MicroservicioException;
import com.canchapro.ms_reportes.exception.ReporteNoEncontradoException;
import com.canchapro.ms_reportes.service.ReporteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
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

class ReporteControllerTest {

    private MockMvc mockMvc;
    private ReporteService service;

    @BeforeEach
    void setUp() {
        service = mock(ReporteService.class);

        ReporteController controller = new ReporteController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listarTodosDebeRetornarStatusOk() throws Exception {
        when(service.listarTodos())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isOk());

        verify(service).listarTodos();
    }

    @Test
    void buscarPorIdDebeRetornarStatusOkCuandoExiste() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(null);

        mockMvc.perform(get("/api/reportes/1"))
                .andExpect(status().isOk());

        verify(service).buscarPorId(1L);
    }

    @Test
    void buscarPorIdDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new ReporteNoEncontradoException(
                        "No existe reporte con ID: 99"
                ));

        mockMvc.perform(get("/api/reportes/99"))
                .andExpect(status().isNotFound());

        verify(service).buscarPorId(99L);
    }

    @Test
    void crearDebeRetornarCreatedCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "titulo": "Reporte mensual",
                    "tipo": "RESERVAS",
                    "descripcion": "Reporte mensual de reservas",
                    "totalRegistros": 10
                }
                """;

        when(service.crear(any()))
                .thenReturn(null);

        mockMvc.perform(post("/api/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(service).crear(any());
    }

    @Test
    void crearDebeRetornarBadRequestCuandoRequestEsInvalido() throws Exception {
        String json = """
                {
                    "titulo": "",
                    "tipo": null,
                    "descripcion": "",
                    "totalRegistros": -1
                }
                """;

        mockMvc.perform(post("/api/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generarDebeRetornarCreatedCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "tipo": "GENERAL"
                }
                """;

        when(service.generar(any()))
                .thenReturn(null);

        mockMvc.perform(post("/api/reportes/generar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(service).generar(any());
    }

    @Test
    void generarDebeRetornarBadRequestCuandoRequestEsInvalido() throws Exception {
        String json = """
                {
                    "tipo": null
                }
                """;

        mockMvc.perform(post("/api/reportes/generar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generarDebeRetornarServiceUnavailableCuandoFallaMicroservicio() throws Exception {
        String json = """
                {
                    "tipo": "GENERAL"
                }
                """;

        when(service.generar(any()))
                .thenThrow(new MicroservicioException(
                        "No se pudo obtener informacion desde otros microservicios"
                ));

        mockMvc.perform(post("/api/reportes/generar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isServiceUnavailable());

        verify(service).generar(any());
    }

    @Test
    void actualizarDebeRetornarStatusOkCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "titulo": "Reporte actualizado",
                    "tipo": "PAGOS",
                    "descripcion": "Reporte actualizado de pagos",
                    "totalRegistros": 5
                }
                """;

        when(service.actualizar(eq(1L), any()))
                .thenReturn(null);

        mockMvc.perform(put("/api/reportes/1")
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

        mockMvc.perform(delete("/api/reportes/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void eliminarDebeRetornarNotFoundCuandoNoExiste() throws Exception {
        doThrow(new ReporteNoEncontradoException(
                "No existe reporte con ID: 99"
        ))
                .when(service)
                .eliminar(99L);

        mockMvc.perform(delete("/api/reportes/99"))
                .andExpect(status().isNotFound());

        verify(service).eliminar(99L);
    }

    @Test
    void cambiarEstadoDebeRetornarStatusOkCuandoRequestEsValido() throws Exception {
        String json = """
                {
                    "estado": "ARCHIVADO"
                }
                """;

        when(service.cambiarEstado(eq(1L), any()))
                .thenReturn(null);

        mockMvc.perform(patch("/api/reportes/1/estado")
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

        mockMvc.perform(patch("/api/reportes/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarPorTipoDebeRetornarStatusOk() throws Exception {
        when(service.listarPorTipo(TipoReporte.RESERVAS))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/reportes/tipo/RESERVAS"))
                .andExpect(status().isOk());

        verify(service).listarPorTipo(TipoReporte.RESERVAS);
    }

    @Test
    void listarPorEstadoDebeRetornarStatusOk() throws Exception {
        when(service.listarPorEstado(EstadoReporte.GENERADO))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/reportes/estado/GENERADO"))
                .andExpect(status().isOk());

        verify(service).listarPorEstado(EstadoReporte.GENERADO);
    }

    @Test
    void listarPorFechasDebeRetornarStatusOk() throws Exception {
        LocalDateTime inicio = LocalDateTime.parse("2026-07-01T10:30:15");
        LocalDateTime fin = LocalDateTime.parse("2026-07-31T22:45:30");

        when(service.listarPorFechas(inicio, fin))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/reportes/fechas")
                        .param("inicio", "2026-07-01T10:30:15")
                        .param("fin", "2026-07-31T22:45:30"))
                .andExpect(status().isOk());

        verify(service).listarPorFechas(inicio, fin);
    }
}