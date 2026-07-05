package com.canchapro.ms_reportes.service;

import com.canchapro.ms_reportes.client.CalificacionClient;
import com.canchapro.ms_reportes.client.PagoClient;
import com.canchapro.ms_reportes.client.ReservaClient;
import com.canchapro.ms_reportes.dto.CambiarEstadoReporteDTO;
import com.canchapro.ms_reportes.dto.GenerarReporteRequestDTO;
import com.canchapro.ms_reportes.dto.ReporteRequestDTO;
import com.canchapro.ms_reportes.dto.ReporteResponseDTO;
import com.canchapro.ms_reportes.entity.EstadoReporte;
import com.canchapro.ms_reportes.entity.Reporte;
import com.canchapro.ms_reportes.entity.TipoReporte;
import com.canchapro.ms_reportes.exception.ReporteNoEncontradoException;
import com.canchapro.ms_reportes.repository.ReporteRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository repository;

    @Mock
    private ReservaClient reservaClient;

    @Mock
    private PagoClient pagoClient;

    @Mock
    private CalificacionClient calificacionClient;

    @InjectMocks
    private ReporteService service;

    @Test
    void crearDebeGuardarReporteConEstadoGenerado() {
        ReporteRequestDTO request = new ReporteRequestDTO(
                "Reporte de reservas",
                TipoReporte.RESERVAS,
                "Reporte manual de reservas",
                5
        );

        Reporte reporteGuardado = Reporte.builder()
                .id(1L)
                .titulo("Reporte de reservas")
                .tipo(TipoReporte.RESERVAS)
                .descripcion("Reporte manual de reservas")
                .totalRegistros(5)
                .estado(EstadoReporte.GENERADO)
                .fechaGeneracion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        when(repository.save(any(Reporte.class)))
                .thenReturn(reporteGuardado);

        ReporteResponseDTO respuesta = service.crear(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Reporte de reservas", respuesta.getTitulo());
        assertEquals(TipoReporte.RESERVAS, respuesta.getTipo());
        assertEquals(5, respuesta.getTotalRegistros());
        assertEquals(EstadoReporte.GENERADO, respuesta.getEstado());

        verify(repository).save(any(Reporte.class));
    }

    @Test
    void generarDebeCrearReporteDeReservasUsandoReservaClient() {
        GenerarReporteRequestDTO request = new GenerarReporteRequestDTO(
                TipoReporte.RESERVAS
        );

        Reporte reporteGuardado = Reporte.builder()
                .id(1L)
                .titulo("Reporte de reservas")
                .tipo(TipoReporte.RESERVAS)
                .descripcion("Reporte generado automáticamente para el módulo reservas. Total de registros encontrados: 3")
                .totalRegistros(3)
                .estado(EstadoReporte.GENERADO)
                .fechaGeneracion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        when(reservaClient.listarReservas())
                .thenReturn(List.of(new Object(), new Object(), new Object()));

        when(repository.save(any(Reporte.class)))
                .thenReturn(reporteGuardado);

        ReporteResponseDTO respuesta = service.generar(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(TipoReporte.RESERVAS, respuesta.getTipo());
        assertEquals(3, respuesta.getTotalRegistros());
        assertEquals(EstadoReporte.GENERADO, respuesta.getEstado());

        verify(reservaClient).listarReservas();
        verify(repository).save(any(Reporte.class));
    }

    @Test
    void generarDebeCrearReporteGeneralSumandoReservasPagosYCalificaciones() {
        GenerarReporteRequestDTO request = new GenerarReporteRequestDTO(
                TipoReporte.GENERAL
        );

        Reporte reporteGuardado = Reporte.builder()
                .id(1L)
                .titulo("Reporte de general")
                .tipo(TipoReporte.GENERAL)
                .descripcion("Reporte generado automáticamente para el módulo general. Total de registros encontrados: 6")
                .totalRegistros(6)
                .estado(EstadoReporte.GENERADO)
                .fechaGeneracion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        when(reservaClient.listarReservas())
                .thenReturn(List.of(new Object(), new Object()));

        when(pagoClient.listarPagos())
                .thenReturn(List.of(new Object(), new Object(), new Object()));

        when(calificacionClient.listarCalificaciones())
                .thenReturn(List.of(new Object()));

        when(repository.save(any(Reporte.class)))
                .thenReturn(reporteGuardado);

        ReporteResponseDTO respuesta = service.generar(request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(TipoReporte.GENERAL, respuesta.getTipo());
        assertEquals(6, respuesta.getTotalRegistros());
        assertEquals(EstadoReporte.GENERADO, respuesta.getEstado());

        verify(reservaClient).listarReservas();
        verify(pagoClient).listarPagos();
        verify(calificacionClient).listarCalificaciones();
        verify(repository).save(any(Reporte.class));
    }

    @Test
    void buscarPorIdDebeRetornarReporteCuandoExiste() {
        Reporte reporte = Reporte.builder()
                .id(1L)
                .titulo("Reporte pagos")
                .tipo(TipoReporte.PAGOS)
                .descripcion("Reporte de pagos")
                .totalRegistros(4)
                .estado(EstadoReporte.GENERADO)
                .fechaGeneracion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(reporte));

        ReporteResponseDTO respuesta = service.buscarPorId(1L);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals("Reporte pagos", respuesta.getTitulo());
        assertEquals(TipoReporte.PAGOS, respuesta.getTipo());
        assertEquals(4, respuesta.getTotalRegistros());

        verify(repository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ReporteNoEncontradoException.class,
                () -> service.buscarPorId(99L)
        );

        verify(repository).findById(99L);
    }

    @Test
    void cambiarEstadoDebeActualizarEstadoDelReporte() {
        Reporte reporte = Reporte.builder()
                .id(1L)
                .titulo("Reporte general")
                .tipo(TipoReporte.GENERAL)
                .descripcion("Reporte general")
                .totalRegistros(10)
                .estado(EstadoReporte.GENERADO)
                .fechaGeneracion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        Reporte reporteArchivado = Reporte.builder()
                .id(1L)
                .titulo("Reporte general")
                .tipo(TipoReporte.GENERAL)
                .descripcion("Reporte general")
                .totalRegistros(10)
                .estado(EstadoReporte.ARCHIVADO)
                .fechaGeneracion(LocalDateTime.of(2026, 7, 1, 20, 0))
                .build();

        CambiarEstadoReporteDTO request = new CambiarEstadoReporteDTO(
                EstadoReporte.ARCHIVADO
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(reporte));

        when(repository.save(any(Reporte.class)))
                .thenReturn(reporteArchivado);

        ReporteResponseDTO respuesta = service.cambiarEstado(1L, request);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getId());
        assertEquals(EstadoReporte.ARCHIVADO, respuesta.getEstado());

        verify(repository).findById(1L);
        verify(repository).save(any(Reporte.class));
    }

    @Test
    void eliminarDebeEliminarReporteCuandoExiste() {
        when(repository.existsById(1L))
                .thenReturn(true);

        service.eliminar(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.existsById(99L))
                .thenReturn(false);

        assertThrows(
                ReporteNoEncontradoException.class,
                () -> service.eliminar(99L)
        );

        verify(repository).existsById(99L);
        verify(repository, never()).deleteById(99L);
    }
}