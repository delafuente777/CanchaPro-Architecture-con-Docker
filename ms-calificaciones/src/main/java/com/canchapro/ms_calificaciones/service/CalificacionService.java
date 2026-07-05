package com.canchapro.ms_calificaciones.service;

import com.canchapro.ms_calificaciones.client.CanchaClient;
import com.canchapro.ms_calificaciones.client.ReservaClient;
import com.canchapro.ms_calificaciones.client.UsuarioClient;
import com.canchapro.ms_calificaciones.dto.CalificacionRequestDTO;
import com.canchapro.ms_calificaciones.dto.CalificacionResponseDTO;
import com.canchapro.ms_calificaciones.dto.CalificacionResumenDTO;
import com.canchapro.ms_calificaciones.entity.Calificacion;
import com.canchapro.ms_calificaciones.exception.CalificacionException;
import com.canchapro.ms_calificaciones.exception.CalificacionNoEncontradaException;
import com.canchapro.ms_calificaciones.exception.MicroservicioException;
import com.canchapro.ms_calificaciones.mapper.CalificacionMapper;
import com.canchapro.ms_calificaciones.repository.CalificacionRepository;

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
public class CalificacionService {

    private final CalificacionRepository repository;
    private final UsuarioClient usuarioClient;
    private final CanchaClient canchaClient;
    private final ReservaClient reservaClient;

    public List<CalificacionResponseDTO> listarTodas() {

        log.info("Listando todas las calificaciones");

        return repository.findAll()
                .stream()
                .map(CalificacionMapper::toResponseDTO)
                .toList();
    }

    public CalificacionResponseDTO buscarPorId(Long id) {

        Long calificacionId = Objects.requireNonNull(
                id,
                "El ID de la calificación no puede ser null"
        );

        Calificacion calificacion = repository.findById(calificacionId)
                .orElseThrow(
                        () -> new CalificacionNoEncontradaException(
                                "No existe calificación con ID: " + calificacionId
                        )
                );

        return CalificacionMapper.toResponseDTO(calificacion);
    }

    public CalificacionResponseDTO crear(
            CalificacionRequestDTO request
    ) {

        CalificacionRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de calificación no puede ser null"
        );

        validarUsuario(requestValidado.getUsuarioId());
        validarCancha(requestValidado.getCanchaId());
        validarReserva(requestValidado.getReservaId());

        if (repository.existsByReservaId(requestValidado.getReservaId())) {
            throw new CalificacionException(
                    "La reserva ya tiene una calificación registrada"
            );
        }

        Calificacion calificacion = Calificacion.builder()
                .usuarioId(requestValidado.getUsuarioId())
                .canchaId(requestValidado.getCanchaId())
                .reservaId(requestValidado.getReservaId())
                .puntuacion(requestValidado.getPuntuacion())
                .comentario(requestValidado.getComentario())
                .fechaCreacion(LocalDateTime.now())
                .build();

        Calificacion guardada = repository.save(calificacion);

        log.info(
                "Calificación creada correctamente con ID {}",
                guardada.getId()
        );

        return CalificacionMapper.toResponseDTO(guardada);
    }

    public CalificacionResponseDTO actualizar(
            Long id,
            CalificacionRequestDTO request
    ) {

        Long calificacionId = Objects.requireNonNull(
                id,
                "El ID de la calificación no puede ser null"
        );

        CalificacionRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de calificación no puede ser null"
        );

        validarUsuario(requestValidado.getUsuarioId());
        validarCancha(requestValidado.getCanchaId());
        validarReserva(requestValidado.getReservaId());

        Calificacion calificacion = repository.findById(calificacionId)
                .orElseThrow(
                        () -> new CalificacionNoEncontradaException(
                                "No existe calificación con ID: " + calificacionId
                        )
                );

        repository.findByReservaId(requestValidado.getReservaId())
                .filter(existente -> !existente.getId().equals(calificacionId))
                .ifPresent(existente -> {
                    throw new CalificacionException(
                            "Ya existe otra calificación para esa reserva"
                    );
                });

        calificacion.setUsuarioId(requestValidado.getUsuarioId());
        calificacion.setCanchaId(requestValidado.getCanchaId());
        calificacion.setReservaId(requestValidado.getReservaId());
        calificacion.setPuntuacion(requestValidado.getPuntuacion());
        calificacion.setComentario(requestValidado.getComentario());

        Calificacion actualizada = repository.save(calificacion);

        log.info(
                "Calificación actualizada correctamente con ID {}",
                calificacionId
        );

        return CalificacionMapper.toResponseDTO(actualizada);
    }

    public void eliminar(Long id) {

        Long calificacionId = Objects.requireNonNull(
                id,
                "El ID de la calificación no puede ser null"
        );

        if (!repository.existsById(calificacionId)) {
            throw new CalificacionNoEncontradaException(
                    "No existe calificación con ID: " + calificacionId
            );
        }

        repository.deleteById(calificacionId);

        log.info(
                "Calificación eliminada correctamente con ID {}",
                calificacionId
        );
    }

    public List<CalificacionResponseDTO> listarPorUsuario(Long usuarioId) {

        Long usuarioIdValidado = Objects.requireNonNull(
                usuarioId,
                "El ID del usuario no puede ser null"
        );

        return repository.findByUsuarioId(usuarioIdValidado)
                .stream()
                .map(CalificacionMapper::toResponseDTO)
                .toList();
    }

    public List<CalificacionResponseDTO> listarPorCancha(Long canchaId) {

        Long canchaIdValidada = Objects.requireNonNull(
                canchaId,
                "El ID de la cancha no puede ser null"
        );

        return repository.findByCanchaId(canchaIdValidada)
                .stream()
                .map(CalificacionMapper::toResponseDTO)
                .toList();
    }

    public CalificacionResponseDTO buscarPorReserva(Long reservaId) {

        Long reservaIdValidada = Objects.requireNonNull(
                reservaId,
                "El ID de la reserva no puede ser null"
        );

        Calificacion calificacion = repository.findByReservaId(reservaIdValidada)
                .orElseThrow(
                        () -> new CalificacionNoEncontradaException(
                                "No existe calificación para la reserva ID: "
                                        + reservaIdValidada
                        )
                );

        return CalificacionMapper.toResponseDTO(calificacion);
    }

    public CalificacionResumenDTO obtenerResumenPorCancha(Long canchaId) {

        Long canchaIdValidada = Objects.requireNonNull(
                canchaId,
                "El ID de la cancha no puede ser null"
        );

        List<Calificacion> calificaciones = repository.findByCanchaId(
                canchaIdValidada
        );

        Double promedio = calificaciones
                .stream()
                .mapToInt(Calificacion::getPuntuacion)
                .average()
                .orElse(0.0);

        return CalificacionResumenDTO.builder()
                .canchaId(canchaIdValidada)
                .promedio(promedio)
                .totalCalificaciones(calificaciones.size())
                .build();
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

    private void validarCancha(Long canchaId) {

        try {
            canchaClient.buscarCanchaPorId(canchaId);
        } catch (FeignException ex) {
            throw new MicroservicioException(
                    "No se pudo validar la cancha ID: " + canchaId
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