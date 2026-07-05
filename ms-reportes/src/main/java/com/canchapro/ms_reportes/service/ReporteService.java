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
import com.canchapro.ms_reportes.exception.MicroservicioException;
import com.canchapro.ms_reportes.exception.ReporteNoEncontradoException;
import com.canchapro.ms_reportes.mapper.ReporteMapper;
import com.canchapro.ms_reportes.repository.ReporteRepository;

import feign.FeignException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteService {

    private final ReporteRepository repository;
    private final ReservaClient reservaClient;
    private final PagoClient pagoClient;
    private final CalificacionClient calificacionClient;

    public List<ReporteResponseDTO> listarTodos() {

        log.info("Listando todos los reportes");

        return repository.findAll()
                .stream()
                .map(ReporteMapper::toResponseDTO)
                .toList();
    }

    public ReporteResponseDTO buscarPorId(Long id) {

        Long reporteId = Objects.requireNonNull(
                id,
                "El ID del reporte no puede ser null"
        );

        Reporte reporte = repository.findById(reporteId)
                .orElseThrow(
                        () -> new ReporteNoEncontradoException(
                                "No existe reporte con ID: " + reporteId
                        )
                );

        return ReporteMapper.toResponseDTO(reporte);
    }

    public ReporteResponseDTO crear(
            ReporteRequestDTO request
    ) {

        ReporteRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de reporte no puede ser null"
        );

        Reporte reporte = Reporte.builder()
                .titulo(requestValidado.getTitulo())
                .tipo(requestValidado.getTipo())
                .descripcion(requestValidado.getDescripcion())
                .totalRegistros(requestValidado.getTotalRegistros())
                .estado(EstadoReporte.GENERADO)
                .fechaGeneracion(LocalDateTime.now())
                .build();

        Reporte guardado = repository.save(reporte);

        log.info(
                "Reporte creado correctamente con ID {}",
                guardado.getId()
        );

        return ReporteMapper.toResponseDTO(guardado);
    }

    public ReporteResponseDTO generar(
            GenerarReporteRequestDTO request
    ) {

        GenerarReporteRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud para generar reporte no puede ser null"
        );

        TipoReporte tipo = Objects.requireNonNull(
                requestValidado.getTipo(),
                "El tipo de reporte no puede ser null"
        );

        Integer total = obtenerTotalPorTipo(tipo);

        Reporte reporte = Reporte.builder()
                .titulo(generarTitulo(tipo))
                .tipo(tipo)
                .descripcion(generarDescripcion(tipo, total))
                .totalRegistros(total)
                .estado(EstadoReporte.GENERADO)
                .fechaGeneracion(LocalDateTime.now())
                .build();

        Reporte guardado = repository.save(reporte);

        log.info(
                "Reporte generado correctamente con ID {}",
                guardado.getId()
        );

        return ReporteMapper.toResponseDTO(guardado);
    }

    public ReporteResponseDTO actualizar(
            Long id,
            ReporteRequestDTO request
    ) {

        Long reporteId = Objects.requireNonNull(
                id,
                "El ID del reporte no puede ser null"
        );

        ReporteRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de reporte no puede ser null"
        );

        Reporte reporte = repository.findById(reporteId)
                .orElseThrow(
                        () -> new ReporteNoEncontradoException(
                                "No existe reporte con ID: " + reporteId
                        )
                );

        reporte.setTitulo(requestValidado.getTitulo());
        reporte.setTipo(requestValidado.getTipo());
        reporte.setDescripcion(requestValidado.getDescripcion());
        reporte.setTotalRegistros(requestValidado.getTotalRegistros());

        Reporte actualizado = repository.save(reporte);

        log.info(
                "Reporte actualizado correctamente con ID {}",
                reporteId
        );

        return ReporteMapper.toResponseDTO(actualizado);
    }

    public void eliminar(Long id) {

        Long reporteId = Objects.requireNonNull(
                id,
                "El ID del reporte no puede ser null"
        );

        if (!repository.existsById(reporteId)) {
            throw new ReporteNoEncontradoException(
                    "No existe reporte con ID: " + reporteId
            );
        }

        repository.deleteById(reporteId);

        log.info(
                "Reporte eliminado correctamente con ID {}",
                reporteId
        );
    }

    public ReporteResponseDTO cambiarEstado(
            Long id,
            CambiarEstadoReporteDTO request
    ) {

        Long reporteId = Objects.requireNonNull(
                id,
                "El ID del reporte no puede ser null"
        );

        CambiarEstadoReporteDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de cambio de estado no puede ser null"
        );

        Reporte reporte = repository.findById(reporteId)
                .orElseThrow(
                        () -> new ReporteNoEncontradoException(
                                "No existe reporte con ID: " + reporteId
                        )
                );

        reporte.setEstado(requestValidado.getEstado());

        Reporte actualizado = repository.save(reporte);

        log.info(
                "Estado de reporte ID {} cambiado a {}",
                reporteId,
                requestValidado.getEstado()
        );

        return ReporteMapper.toResponseDTO(actualizado);
    }

    public List<ReporteResponseDTO> listarPorTipo(
            TipoReporte tipo
    ) {

        TipoReporte tipoValidado = Objects.requireNonNull(
                tipo,
                "El tipo de reporte no puede ser null"
        );

        return repository.findByTipo(tipoValidado)
                .stream()
                .map(ReporteMapper::toResponseDTO)
                .toList();
    }

    public List<ReporteResponseDTO> listarPorEstado(
            EstadoReporte estado
    ) {

        EstadoReporte estadoValidado = Objects.requireNonNull(
                estado,
                "El estado del reporte no puede ser null"
        );

        return repository.findByEstado(estadoValidado)
                .stream()
                .map(ReporteMapper::toResponseDTO)
                .toList();
    }

    public List<ReporteResponseDTO> listarPorFechas(
            LocalDateTime inicio,
            LocalDateTime fin
    ) {

        LocalDateTime inicioValidado = Objects.requireNonNull(
                inicio,
                "La fecha de inicio no puede ser null"
        );

        LocalDateTime finValidado = Objects.requireNonNull(
                fin,
                "La fecha de fin no puede ser null"
        );

        return repository.findByFechaGeneracionBetween(
                        inicioValidado,
                        finValidado
                )
                .stream()
                .map(ReporteMapper::toResponseDTO)
                .toList();
    }

    private Integer obtenerTotalPorTipo(
            TipoReporte tipo
    ) {

        try {
            if (TipoReporte.RESERVAS.equals(tipo)) {
                return reservaClient.listarReservas().size();
            }

            if (TipoReporte.PAGOS.equals(tipo)) {
                return pagoClient.listarPagos().size();
            }

            if (TipoReporte.CALIFICACIONES.equals(tipo)) {
                return calificacionClient.listarCalificaciones().size();
            }

            Integer totalReservas = reservaClient.listarReservas().size();
            Integer totalPagos = pagoClient.listarPagos().size();
            Integer totalCalificaciones = calificacionClient
                    .listarCalificaciones()
                    .size();

            return totalReservas + totalPagos + totalCalificaciones;

        } catch (FeignException ex) {
            throw new MicroservicioException(
                    "No se pudo obtener información desde otros microservicios"
            );
        }
    }

    private String generarTitulo(
            TipoReporte tipo
    ) {

        return "Reporte de " + tipo.name().toLowerCase();
    }

    private String generarDescripcion(
            TipoReporte tipo,
            Integer total
    ) {

        return "Reporte generado automáticamente para el módulo "
                + tipo.name().toLowerCase()
                + ". Total de registros encontrados: "
                + total;
    }
}