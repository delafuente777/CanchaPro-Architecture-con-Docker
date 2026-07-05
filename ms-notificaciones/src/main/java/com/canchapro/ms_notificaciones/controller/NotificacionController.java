package com.canchapro.ms_notificaciones.controller;

import com.canchapro.ms_notificaciones.dto.CambiarEstadoNotificacionDTO;
import com.canchapro.ms_notificaciones.dto.NotificacionRequestDTO;
import com.canchapro.ms_notificaciones.dto.NotificacionResponseDTO;
import com.canchapro.ms_notificaciones.entity.EstadoNotificacion;
import com.canchapro.ms_notificaciones.entity.TipoNotificacion;
import com.canchapro.ms_notificaciones.service.NotificacionService;

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
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Notificaciones",
        description = "Gestión de notificaciones del sistema CanchaPro"
)
public class NotificacionController {

    private final NotificacionService service;

    @Operation(summary = "Listar notificaciones")
    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listarTodas() {

        return ResponseEntity.ok(
                service.listarTodas()
        );
    }

    @Operation(summary = "Buscar notificación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> buscarPorId(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @Operation(summary = "Crear notificación")
    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crear(
            @Valid
            @RequestBody NotificacionRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @Operation(summary = "Actualizar notificación")
    @PutMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> actualizar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody NotificacionRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.actualizar(id, request)
        );
    }

    @Operation(summary = "Eliminar notificación")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Marcar notificación como enviada")
    @PatchMapping("/{id}/enviar")
    public ResponseEntity<NotificacionResponseDTO> marcarEnviada(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.marcarEnviada(id)
        );
    }

    @Operation(summary = "Marcar notificación como leída")
    @PatchMapping("/{id}/leer")
    public ResponseEntity<NotificacionResponseDTO> marcarLeida(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.marcarLeida(id)
        );
    }

    @Operation(summary = "Marcar notificación como fallida")
    @PatchMapping("/{id}/fallar")
    public ResponseEntity<NotificacionResponseDTO> marcarFallida(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id
    ) {

        return ResponseEntity.ok(
                service.marcarFallida(id)
        );
    }

    @Operation(summary = "Cambiar estado de notificación")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<NotificacionResponseDTO> cambiarEstado(
            @PathVariable
            @Positive(message = "El ID debe ser mayor que cero")
            Long id,

            @Valid
            @RequestBody CambiarEstadoNotificacionDTO request
    ) {

        return ResponseEntity.ok(
                service.cambiarEstado(id, request)
        );
    }

    @Operation(summary = "Listar notificaciones por usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorUsuario(
            @PathVariable
            @Positive(message = "El ID de usuario debe ser mayor que cero")
            Long usuarioId
    ) {

        return ResponseEntity.ok(
                service.listarPorUsuario(usuarioId)
        );
    }

    @Operation(summary = "Listar notificaciones por reserva")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorReserva(
            @PathVariable
            @Positive(message = "El ID de reserva debe ser mayor que cero")
            Long reservaId
    ) {

        return ResponseEntity.ok(
                service.listarPorReserva(reservaId)
        );
    }

    @Operation(summary = "Listar notificaciones por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorEstado(
            @PathVariable EstadoNotificacion estado
    ) {

        return ResponseEntity.ok(
                service.listarPorEstado(estado)
        );
    }

    @Operation(summary = "Listar notificaciones por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorTipo(
            @PathVariable TipoNotificacion tipo
    ) {

        return ResponseEntity.ok(
                service.listarPorTipo(tipo)
        );
    }

    @Operation(summary = "Listar notificaciones por usuario y estado")
    @GetMapping("/usuario/{usuarioId}/estado/{estado}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorUsuarioYEstado(
            @PathVariable
            @Positive(message = "El ID de usuario debe ser mayor que cero")
            Long usuarioId,

            @PathVariable EstadoNotificacion estado
    ) {

        return ResponseEntity.ok(
                service.listarPorUsuarioYEstado(
                        usuarioId,
                        estado
                )
        );
    }
}