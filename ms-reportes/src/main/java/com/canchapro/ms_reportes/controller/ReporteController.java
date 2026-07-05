package com.canchapro.ms_reportes.controller;

import com.canchapro.ms_reportes.dto.CambiarEstadoReporteDTO;
import com.canchapro.ms_reportes.dto.GenerarReporteRequestDTO;
import com.canchapro.ms_reportes.dto.ReporteRequestDTO;
import com.canchapro.ms_reportes.dto.ReporteResponseDTO;
import com.canchapro.ms_reportes.entity.EstadoReporte;
import com.canchapro.ms_reportes.entity.TipoReporte;
import com.canchapro.ms_reportes.service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Reportes",
        description = "Gestion y generacion de reportes del sistema CanchaPro"
)
public class ReporteController {

    private final ReporteService service;

    @Operation(summary = "Listar reportes")
    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @Operation(summary = "Buscar reporte por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> buscarPorId(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @Operation(summary = "Crear reporte manual")
    @PostMapping
    public ResponseEntity<ReporteResponseDTO> crear(
            @Valid
            @RequestBody ReporteRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @Operation(summary = "Generar reporte automatico")
    @PostMapping("/generar")
    public ResponseEntity<ReporteResponseDTO> generar(
            @Valid
            @RequestBody GenerarReporteRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.generar(request));
    }

    @Operation(summary = "Actualizar reporte")
    @PutMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> actualizar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody ReporteRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.actualizar(id, request)
        );
    }

    @Operation(summary = "Eliminar reporte")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar estado de reporte")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReporteResponseDTO> cambiarEstado(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody CambiarEstadoReporteDTO request
    ) {

        return ResponseEntity.ok(
                service.cambiarEstado(id, request)
        );
    }

    @Operation(summary = "Listar reportes por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ReporteResponseDTO>> listarPorTipo(
            @PathVariable TipoReporte tipo
    ) {

        return ResponseEntity.ok(
                service.listarPorTipo(tipo)
        );
    }

    @Operation(summary = "Listar reportes por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReporteResponseDTO>> listarPorEstado(
            @PathVariable EstadoReporte estado
    ) {

        return ResponseEntity.ok(
                service.listarPorEstado(estado)
        );
    }

    @Operation(summary = "Listar reportes por rango de fechas")
    @GetMapping("/fechas")
    public ResponseEntity<List<ReporteResponseDTO>> listarPorFechas(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fin
    ) {

        return ResponseEntity.ok(
                service.listarPorFechas(
                        inicio,
                        fin
                )
        );
    }
}