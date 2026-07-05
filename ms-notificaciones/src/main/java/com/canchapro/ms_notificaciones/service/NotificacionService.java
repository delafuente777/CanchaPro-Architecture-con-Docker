package com.canchapro.ms_notificaciones.service;

import com.canchapro.ms_notificaciones.client.ReservaClient;
import com.canchapro.ms_notificaciones.client.UsuarioClient;
import com.canchapro.ms_notificaciones.dto.CambiarEstadoNotificacionDTO;
import com.canchapro.ms_notificaciones.dto.NotificacionRequestDTO;
import com.canchapro.ms_notificaciones.dto.NotificacionResponseDTO;
import com.canchapro.ms_notificaciones.entity.EstadoNotificacion;
import com.canchapro.ms_notificaciones.entity.Notificacion;
import com.canchapro.ms_notificaciones.entity.TipoNotificacion;
import com.canchapro.ms_notificaciones.exception.MicroservicioException;
import com.canchapro.ms_notificaciones.exception.NotificacionException;
import com.canchapro.ms_notificaciones.exception.NotificacionNoEncontradaException;
import com.canchapro.ms_notificaciones.mapper.NotificacionMapper;
import com.canchapro.ms_notificaciones.repository.NotificacionRepository;

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
public class NotificacionService {

    private final NotificacionRepository repository;
    private final UsuarioClient usuarioClient;
    private final ReservaClient reservaClient;

    public List<NotificacionResponseDTO> listarTodas() {

        log.info("Listando todas las notificaciones");

        return repository.findAll()
                .stream()
                .map(NotificacionMapper::toResponseDTO)
                .toList();
    }

    public NotificacionResponseDTO buscarPorId(Long id) {

        Long notificacionId = Objects.requireNonNull(
                id,
                "El ID de la notificación no puede ser null"
        );

        Notificacion notificacion = repository.findById(notificacionId)
                .orElseThrow(
                        () -> new NotificacionNoEncontradaException(
                                "No existe notificación con ID: " + notificacionId
                        )
                );

        return NotificacionMapper.toResponseDTO(notificacion);
    }

    public NotificacionResponseDTO crear(
            NotificacionRequestDTO request
    ) {

        NotificacionRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de notificación no puede ser null"
        );

        validarUsuario(requestValidado.getUsuarioId());

        if (requestValidado.getReservaId() != null) {
            validarReserva(requestValidado.getReservaId());
        }

        Notificacion notificacion = Notificacion.builder()
                .usuarioId(requestValidado.getUsuarioId())
                .reservaId(requestValidado.getReservaId())
                .titulo(requestValidado.getTitulo())
                .mensaje(requestValidado.getMensaje())
                .tipo(requestValidado.getTipo())
                .estado(EstadoNotificacion.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .fechaEnvio(null)
                .build();

        Notificacion guardada = repository.save(notificacion);

        log.info(
                "Notificación creada correctamente con ID {}",
                guardada.getId()
        );

        return NotificacionMapper.toResponseDTO(guardada);
    }

    public NotificacionResponseDTO actualizar(
            Long id,
            NotificacionRequestDTO request
    ) {

        Long notificacionId = Objects.requireNonNull(
                id,
                "El ID de la notificación no puede ser null"
        );

        NotificacionRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de notificación no puede ser null"
        );

        Notificacion notificacion = repository.findById(notificacionId)
                .orElseThrow(
                        () -> new NotificacionNoEncontradaException(
                                "No existe notificación con ID: " + notificacionId
                        )
                );

        if (EstadoNotificacion.LEIDA.equals(notificacion.getEstado())) {
            throw new NotificacionException(
                    "No se puede actualizar una notificación leída"
            );
        }

        validarUsuario(requestValidado.getUsuarioId());

        if (requestValidado.getReservaId() != null) {
            validarReserva(requestValidado.getReservaId());
        }

        notificacion.setUsuarioId(requestValidado.getUsuarioId());
        notificacion.setReservaId(requestValidado.getReservaId());
        notificacion.setTitulo(requestValidado.getTitulo());
        notificacion.setMensaje(requestValidado.getMensaje());
        notificacion.setTipo(requestValidado.getTipo());

        Notificacion actualizada = repository.save(notificacion);

        log.info(
                "Notificación actualizada correctamente con ID {}",
                notificacionId
        );

        return NotificacionMapper.toResponseDTO(actualizada);
    }

    public void eliminar(Long id) {

        Long notificacionId = Objects.requireNonNull(
                id,
                "El ID de la notificación no puede ser null"
        );

        if (!repository.existsById(notificacionId)) {
            throw new NotificacionNoEncontradaException(
                    "No existe notificación con ID: " + notificacionId
            );
        }

        repository.deleteById(notificacionId);

        log.info(
                "Notificación eliminada correctamente con ID {}",
                notificacionId
        );
    }

    public NotificacionResponseDTO marcarEnviada(Long id) {

        Notificacion notificacion = obtenerNotificacion(id);

        if (EstadoNotificacion.LEIDA.equals(notificacion.getEstado())) {
            throw new NotificacionException(
                    "La notificación ya fue leída"
            );
        }

        notificacion.setEstado(EstadoNotificacion.ENVIADA);
        notificacion.setFechaEnvio(LocalDateTime.now());

        Notificacion actualizada = repository.save(notificacion);

        log.info(
                "Notificación marcada como enviada con ID {}",
                id
        );

        return NotificacionMapper.toResponseDTO(actualizada);
    }

    public NotificacionResponseDTO marcarLeida(Long id) {

        Notificacion notificacion = obtenerNotificacion(id);

        notificacion.setEstado(EstadoNotificacion.LEIDA);

        if (notificacion.getFechaEnvio() == null) {
            notificacion.setFechaEnvio(LocalDateTime.now());
        }

        Notificacion actualizada = repository.save(notificacion);

        log.info(
                "Notificación marcada como leída con ID {}",
                id
        );

        return NotificacionMapper.toResponseDTO(actualizada);
    }

    public NotificacionResponseDTO marcarFallida(Long id) {

        Notificacion notificacion = obtenerNotificacion(id);

        if (EstadoNotificacion.LEIDA.equals(notificacion.getEstado())) {
            throw new NotificacionException(
                    "No se puede marcar como fallida una notificación leída"
            );
        }

        notificacion.setEstado(EstadoNotificacion.FALLIDA);

        Notificacion actualizada = repository.save(notificacion);

        log.info(
                "Notificación marcada como fallida con ID {}",
                id
        );

        return NotificacionMapper.toResponseDTO(actualizada);
    }

    public NotificacionResponseDTO cambiarEstado(
            Long id,
            CambiarEstadoNotificacionDTO request
    ) {

        Long notificacionId = Objects.requireNonNull(
                id,
                "El ID de la notificación no puede ser null"
        );

        CambiarEstadoNotificacionDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de cambio de estado no puede ser null"
        );

        Notificacion notificacion = obtenerNotificacion(notificacionId);

        notificacion.setEstado(requestValidado.getEstado());

        if (EstadoNotificacion.ENVIADA.equals(requestValidado.getEstado())
                || EstadoNotificacion.LEIDA.equals(requestValidado.getEstado())) {
            if (notificacion.getFechaEnvio() == null) {
                notificacion.setFechaEnvio(LocalDateTime.now());
            }
        }

        Notificacion actualizada = repository.save(notificacion);

        log.info(
                "Estado de notificación ID {} cambiado a {}",
                notificacionId,
                requestValidado.getEstado()
        );

        return NotificacionMapper.toResponseDTO(actualizada);
    }

    public List<NotificacionResponseDTO> listarPorUsuario(Long usuarioId) {

        Long usuarioIdValidado = Objects.requireNonNull(
                usuarioId,
                "El ID del usuario no puede ser null"
        );

        return repository.findByUsuarioId(usuarioIdValidado)
                .stream()
                .map(NotificacionMapper::toResponseDTO)
                .toList();
    }

    public List<NotificacionResponseDTO> listarPorReserva(Long reservaId) {

        Long reservaIdValidada = Objects.requireNonNull(
                reservaId,
                "El ID de la reserva no puede ser null"
        );

        return repository.findByReservaId(reservaIdValidada)
                .stream()
                .map(NotificacionMapper::toResponseDTO)
                .toList();
    }

    public List<NotificacionResponseDTO> listarPorEstado(
            EstadoNotificacion estado
    ) {

        EstadoNotificacion estadoValidado = Objects.requireNonNull(
                estado,
                "El estado no puede ser null"
        );

        return repository.findByEstado(estadoValidado)
                .stream()
                .map(NotificacionMapper::toResponseDTO)
                .toList();
    }

    public List<NotificacionResponseDTO> listarPorTipo(
            TipoNotificacion tipo
    ) {

        TipoNotificacion tipoValidado = Objects.requireNonNull(
                tipo,
                "El tipo no puede ser null"
        );

        return repository.findByTipo(tipoValidado)
                .stream()
                .map(NotificacionMapper::toResponseDTO)
                .toList();
    }

    public List<NotificacionResponseDTO> listarPorUsuarioYEstado(
            Long usuarioId,
            EstadoNotificacion estado
    ) {

        Long usuarioIdValidado = Objects.requireNonNull(
                usuarioId,
                "El ID del usuario no puede ser null"
        );

        EstadoNotificacion estadoValidado = Objects.requireNonNull(
                estado,
                "El estado no puede ser null"
        );

        return repository.findByUsuarioIdAndEstado(
                        usuarioIdValidado,
                        estadoValidado
                )
                .stream()
                .map(NotificacionMapper::toResponseDTO)
                .toList();
    }

    private Notificacion obtenerNotificacion(Long id) {

        Long notificacionId = Objects.requireNonNull(
                id,
                "El ID de la notificación no puede ser null"
        );

        return repository.findById(notificacionId)
                .orElseThrow(
                        () -> new NotificacionNoEncontradaException(
                                "No existe notificación con ID: " + notificacionId
                        )
                );
    }

    private void validarUsuario(Long usuarioId) {

        try {
            usuarioClient.buscarUsuarioPorId(usuarioId);
        } catch (FeignException ex) {
            throw new MicroservicioException(
                    "No se pudo validar el usuario ID: " + usuarioId
            );
        }
    }

    private void validarReserva(Long reservaId) {

        try {
            reservaClient.buscarReservaPorId(reservaId);
        } catch (FeignException ex) {
            throw new MicroservicioException(
                    "No se pudo validar la reserva ID: " + reservaId
            );
        }
    }
}