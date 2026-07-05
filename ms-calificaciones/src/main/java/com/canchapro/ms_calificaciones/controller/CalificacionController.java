package com.canchapro.ms_calificaciones.controller;

import com.canchapro.ms_calificaciones.dto.CalificacionRequestDTO;
import com.canchapro.ms_calificaciones.dto.CalificacionResponseDTO;
import com.canchapro.ms_calificaciones.dto.CalificacionResumenDTO;
import com.canchapro.ms_calificaciones.service.CalificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Calificaciones",
        description = "Gestión de calificaciones de canchas y reservas"
)
public class CalificacionController {

    private final CalificacionService service;

    @Operation(summary = "Listar calificaciones")
    @GetMapping
    public ResponseEntity<List<CalificacionResponseDTO>> listarTodas() {

        return ResponseEntity.ok(
                service.listarTodas()
        );
    }

    @Operation(summary = "Buscar calificación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> buscarPorId(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @Operation(summary = "Crear calificación")
    @PostMapping
    public ResponseEntity<CalificacionResponseDTO> crear(
            @Valid
            @RequestBody CalificacionRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @Operation(summary = "Actualizar calificación")
    @PutMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> actualizar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody CalificacionRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.actualizar(id, request)
        );
    }

    @Operation(summary = "Eliminar calificación")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar calificaciones por usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CalificacionResponseDTO>> listarPorUsuario(
            @PathVariable
            @Positive(message = "El ID de usuario debe ser mayor que cero")
            Long usuarioId
    ) {

        return ResponseEntity.ok(
                service.listarPorUsuario(usuarioId)
        );
    }

    @Operation(summary = "Listar calificaciones por cancha")
    @GetMapping("/cancha/{canchaId}")
    public ResponseEntity<List<CalificacionResponseDTO>> listarPorCancha(
            @PathVariable
            @Positive(message = "El ID de cancha debe ser mayor que cero")
            Long canchaId
    ) {

        return ResponseEntity.ok(
                service.listarPorCancha(canchaId)
        );
    }

    @Operation(summary = "Buscar calificación por reserva")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<CalificacionResponseDTO> buscarPorReserva(
            @PathVariable
            @Positive(message = "El ID de reserva debe ser mayor que cero")
            Long reservaId
    ) {

        return ResponseEntity.ok(
                service.buscarPorReserva(reservaId)
        );
    }

    @Operation(summary = "Obtener resumen de calificaciones por cancha")
    @GetMapping("/cancha/{canchaId}/resumen")
    public ResponseEntity<CalificacionResumenDTO> obtenerResumenPorCancha(
            @PathVariable
            @Positive(message = "El ID de cancha debe ser mayor que cero")
            Long canchaId
    ) {

        return ResponseEntity.ok(
                service.obtenerResumenPorCancha(canchaId)
        );
    }
}