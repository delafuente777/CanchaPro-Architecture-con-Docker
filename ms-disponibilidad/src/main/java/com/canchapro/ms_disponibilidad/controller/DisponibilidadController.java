package com.canchapro.ms_disponibilidad.controller;

import com.canchapro.ms_disponibilidad.dto.DisponibilidadRequestDTO;
import com.canchapro.ms_disponibilidad.dto.DisponibilidadResponseDTO;
import com.canchapro.ms_disponibilidad.entity.EstadoDisponibilidad;
import com.canchapro.ms_disponibilidad.service.DisponibilidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Disponibilidad",
        description = "Gestion de disponibilidad de horarios de canchas"
)
public class DisponibilidadController {

    private final DisponibilidadService service;

    @Operation(summary = "Listar disponibilidades")
    @GetMapping
    public ResponseEntity<List<DisponibilidadResponseDTO>> listarTodas() {

        return ResponseEntity.ok(
                service.listarTodas()
        );
    }

    @Operation(summary = "Buscar disponibilidad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadResponseDTO> buscarPorId(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @Operation(summary = "Crear disponibilidad")
    @PostMapping
    public ResponseEntity<DisponibilidadResponseDTO> crear(
            @Valid
            @RequestBody DisponibilidadRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @Operation(summary = "Actualizar disponibilidad")
    @PutMapping("/{id}")
    public ResponseEntity<DisponibilidadResponseDTO> actualizar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody DisponibilidadRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.actualizar(id, request)
        );
    }

    @Operation(summary = "Eliminar disponibilidad")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar horarios por cancha y fecha")
    @GetMapping("/cancha/{canchaId}/fecha/{fecha}")
    public ResponseEntity<List<DisponibilidadResponseDTO>> listarPorCanchaYFecha(
            @PathVariable
            @Positive(message = "El ID de cancha debe ser mayor que cero")
            Long canchaId,

            @PathVariable
            @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha
    ) {

        return ResponseEntity.ok(
                service.listarPorCanchaYFecha(
                        canchaId,
                        fecha
                )
        );
    }

    @Operation(summary = "Listar disponibilidades por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<DisponibilidadResponseDTO>> listarPorEstado(
            @PathVariable EstadoDisponibilidad estado
    ) {

        return ResponseEntity.ok(
                service.listarPorEstado(estado)
        );
    }

    @Operation(summary = "Consultar si un horario esta disponible")
    @GetMapping("/consultar")
    public ResponseEntity<Boolean> consultarDisponible(
            @RequestParam
            @Positive(message = "El ID de cancha debe ser mayor que cero")
            Long canchaId,

            @RequestParam
            @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime horaInicio
    ) {

        return ResponseEntity.ok(
                service.consultarDisponible(
                        canchaId,
                        fecha,
                        horaInicio
                )
        );
    }

    @Operation(summary = "Bloquear horario")
    @PatchMapping("/bloquear")
    public ResponseEntity<DisponibilidadResponseDTO> bloquearHorario(
            @RequestParam
            @Positive(message = "El ID de cancha debe ser mayor que cero")
            Long canchaId,

            @RequestParam
            @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime horaInicio
    ) {

        return ResponseEntity.ok(
                service.bloquearHorario(
                        canchaId,
                        fecha,
                        horaInicio
                )
        );
    }

    @Operation(summary = "Liberar horario")
    @PatchMapping("/liberar")
    public ResponseEntity<DisponibilidadResponseDTO> liberarHorario(
            @RequestParam
            @Positive(message = "El ID de cancha debe ser mayor que cero")
            Long canchaId,

            @RequestParam
            @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime horaInicio
    ) {

        return ResponseEntity.ok(
                service.liberarHorario(
                        canchaId,
                        fecha,
                        horaInicio
                )
        );
    }

    @Operation(summary = "Reservar horario")
    @PatchMapping("/reservar")
    public ResponseEntity<DisponibilidadResponseDTO> reservarHorario(
            @RequestParam
            @Positive(message = "El ID de cancha debe ser mayor que cero")
            Long canchaId,

            @RequestParam
            @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime horaInicio
    ) {

        return ResponseEntity.ok(
                service.reservarHorario(
                        canchaId,
                        fecha,
                        horaInicio
                )
        );
    }
}