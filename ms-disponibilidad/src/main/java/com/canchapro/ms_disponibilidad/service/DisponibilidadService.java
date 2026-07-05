package com.canchapro.ms_disponibilidad.service;

import com.canchapro.ms_disponibilidad.dto.DisponibilidadRequestDTO;
import com.canchapro.ms_disponibilidad.dto.DisponibilidadResponseDTO;
import com.canchapro.ms_disponibilidad.entity.Disponibilidad;
import com.canchapro.ms_disponibilidad.entity.EstadoDisponibilidad;
import com.canchapro.ms_disponibilidad.exception.DisponibilidadNoEncontradaException;
import com.canchapro.ms_disponibilidad.exception.HorarioNoDisponibleException;
import com.canchapro.ms_disponibilidad.exception.HorarioYaExisteException;
import com.canchapro.ms_disponibilidad.mapper.DisponibilidadMapper;
import com.canchapro.ms_disponibilidad.repository.DisponibilidadRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisponibilidadService {

    private final DisponibilidadRepository repository;

    public List<DisponibilidadResponseDTO> listarTodas() {

        log.info("Listando disponibilidades");

        return repository.findAll()
                .stream()
                .map(DisponibilidadMapper::toResponseDTO)
                .toList();
    }

    public DisponibilidadResponseDTO buscarPorId(Long id) {

        Long disponibilidadId = Objects.requireNonNull(
                id,
                "El ID de disponibilidad no puede ser null"
        );

        Disponibilidad disponibilidad = repository.findById(disponibilidadId)
                .orElseThrow(
                        () -> new DisponibilidadNoEncontradaException(
                                "No existe disponibilidad con ID: " + disponibilidadId
                        )
                );

        return DisponibilidadMapper.toResponseDTO(disponibilidad);
    }

    public DisponibilidadResponseDTO crear(
            DisponibilidadRequestDTO request
    ) {

        DisponibilidadRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de disponibilidad no puede ser null"
        );

        validarHorario(
                requestValidado.getHoraInicio(),
                requestValidado.getHoraFin()
        );

        if (repository.existsByCanchaIdAndFechaAndHoraInicio(
                requestValidado.getCanchaId(),
                requestValidado.getFecha(),
                requestValidado.getHoraInicio()
        )) {
            throw new HorarioYaExisteException(
                    "Ya existe disponibilidad para esa cancha, fecha y hora"
            );
        }

        Disponibilidad disponibilidad = Disponibilidad.builder()
                .canchaId(requestValidado.getCanchaId())
                .fecha(requestValidado.getFecha())
                .horaInicio(requestValidado.getHoraInicio())
                .horaFin(requestValidado.getHoraFin())
                .estado(requestValidado.getEstado())
                .build();

        Disponibilidad guardada = repository.save(disponibilidad);

        log.info(
                "Disponibilidad creada con ID {}",
                guardada.getId()
        );

        return DisponibilidadMapper.toResponseDTO(guardada);
    }

    public DisponibilidadResponseDTO actualizar(
            Long id,
            DisponibilidadRequestDTO request
    ) {

        Long disponibilidadId = Objects.requireNonNull(
                id,
                "El ID de disponibilidad no puede ser null"
        );

        DisponibilidadRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de disponibilidad no puede ser null"
        );

        validarHorario(
                requestValidado.getHoraInicio(),
                requestValidado.getHoraFin()
        );

        Disponibilidad disponibilidad = repository.findById(disponibilidadId)
                .orElseThrow(
                        () -> new DisponibilidadNoEncontradaException(
                                "No existe disponibilidad con ID: " + disponibilidadId
                        )
                );

        repository.findByCanchaIdAndFechaAndHoraInicio(
                        requestValidado.getCanchaId(),
                        requestValidado.getFecha(),
                        requestValidado.getHoraInicio()
                )
                .filter(existente -> !existente.getId().equals(disponibilidadId))
                .ifPresent(existente -> {
                    throw new HorarioYaExisteException(
                            "Ya existe otra disponibilidad para esa cancha, fecha y hora"
                    );
                });

        disponibilidad.setCanchaId(requestValidado.getCanchaId());
        disponibilidad.setFecha(requestValidado.getFecha());
        disponibilidad.setHoraInicio(requestValidado.getHoraInicio());
        disponibilidad.setHoraFin(requestValidado.getHoraFin());
        disponibilidad.setEstado(requestValidado.getEstado());

        Disponibilidad actualizada = repository.save(disponibilidad);

        log.info(
                "Disponibilidad actualizada con ID {}",
                disponibilidadId
        );

        return DisponibilidadMapper.toResponseDTO(actualizada);
    }

    public void eliminar(Long id) {

        Long disponibilidadId = Objects.requireNonNull(
                id,
                "El ID de disponibilidad no puede ser null"
        );

        if (!repository.existsById(disponibilidadId)) {
            throw new DisponibilidadNoEncontradaException(
                    "No existe disponibilidad con ID: " + disponibilidadId
            );
        }

        repository.deleteById(disponibilidadId);

        log.info(
                "Disponibilidad eliminada con ID {}",
                disponibilidadId
        );
    }

    public List<DisponibilidadResponseDTO> listarPorCanchaYFecha(
            Long canchaId,
            LocalDate fecha
    ) {

        Long canchaIdValidada = Objects.requireNonNull(
                canchaId,
                "El ID de cancha no puede ser null"
        );

        LocalDate fechaValidada = Objects.requireNonNull(
                fecha,
                "La fecha no puede ser null"
        );

        return repository.findByCanchaIdAndFecha(
                        canchaIdValidada,
                        fechaValidada
                )
                .stream()
                .map(DisponibilidadMapper::toResponseDTO)
                .toList();
    }

    public List<DisponibilidadResponseDTO> listarPorEstado(
            EstadoDisponibilidad estado
    ) {

        EstadoDisponibilidad estadoValidado = Objects.requireNonNull(
                estado,
                "El estado no puede ser null"
        );

        return repository.findByEstado(estadoValidado)
                .stream()
                .map(DisponibilidadMapper::toResponseDTO)
                .toList();
    }

    public Boolean consultarDisponible(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio
    ) {

        Long canchaIdValidada = Objects.requireNonNull(
                canchaId,
                "El ID de cancha no puede ser null"
        );

        LocalDate fechaValidada = Objects.requireNonNull(
                fecha,
                "La fecha no puede ser null"
        );

        LocalTime horaValidada = Objects.requireNonNull(
                horaInicio,
                "La hora de inicio no puede ser null"
        );

        return repository.findByCanchaIdAndFechaAndHoraInicio(
                        canchaIdValidada,
                        fechaValidada,
                        horaValidada
                )
                .map(disponibilidad ->
                        EstadoDisponibilidad.DISPONIBLE.equals(
                                disponibilidad.getEstado()
                        )
                )
                .orElse(false);
    }

    public DisponibilidadResponseDTO bloquearHorario(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio
    ) {

        Disponibilidad disponibilidad = obtenerPorCanchaFechaHora(
                canchaId,
                fecha,
                horaInicio
        );

        if (!EstadoDisponibilidad.DISPONIBLE.equals(disponibilidad.getEstado())) {
            throw new HorarioNoDisponibleException(
                    "El horario no se encuentra disponible"
            );
        }

        disponibilidad.setEstado(EstadoDisponibilidad.BLOQUEADO);

        Disponibilidad actualizada = repository.save(disponibilidad);

        log.info(
                "Horario bloqueado para cancha ID {}",
                canchaId
        );

        return DisponibilidadMapper.toResponseDTO(actualizada);
    }

    public DisponibilidadResponseDTO liberarHorario(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio
    ) {

        Disponibilidad disponibilidad = obtenerPorCanchaFechaHora(
                canchaId,
                fecha,
                horaInicio
        );

        disponibilidad.setEstado(EstadoDisponibilidad.DISPONIBLE);

        Disponibilidad actualizada = repository.save(disponibilidad);

        log.info(
                "Horario liberado para cancha ID {}",
                canchaId
        );

        return DisponibilidadMapper.toResponseDTO(actualizada);
    }

    public DisponibilidadResponseDTO reservarHorario(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio
    ) {

        Disponibilidad disponibilidad = obtenerPorCanchaFechaHora(
                canchaId,
                fecha,
                horaInicio
        );

        if (!EstadoDisponibilidad.DISPONIBLE.equals(disponibilidad.getEstado())) {
            throw new HorarioNoDisponibleException(
                    "El horario no se encuentra disponible para reservar"
            );
        }

        disponibilidad.setEstado(EstadoDisponibilidad.RESERVADO);

        Disponibilidad actualizada = repository.save(disponibilidad);

        log.info(
                "Horario reservado para cancha ID {}",
                canchaId
        );

        return DisponibilidadMapper.toResponseDTO(actualizada);
    }

    private Disponibilidad obtenerPorCanchaFechaHora(
            Long canchaId,
            LocalDate fecha,
            LocalTime horaInicio
    ) {

        Long canchaIdValidada = Objects.requireNonNull(
                canchaId,
                "El ID de cancha no puede ser null"
        );

        LocalDate fechaValidada = Objects.requireNonNull(
                fecha,
                "La fecha no puede ser null"
        );

        LocalTime horaValidada = Objects.requireNonNull(
                horaInicio,
                "La hora de inicio no puede ser null"
        );

        return repository.findByCanchaIdAndFechaAndHoraInicio(
                        canchaIdValidada,
                        fechaValidada,
                        horaValidada
                )
                .orElseThrow(
                        () -> new DisponibilidadNoEncontradaException(
                                "No existe disponibilidad para esa cancha, fecha y hora"
                        )
                );
    }

    private void validarHorario(
            LocalTime horaInicio,
            LocalTime horaFin
    ) {

        LocalTime inicio = Objects.requireNonNull(
                horaInicio,
                "La hora de inicio no puede ser null"
        );

        LocalTime fin = Objects.requireNonNull(
                horaFin,
                "La hora de fin no puede ser null"
        );

        if (!fin.isAfter(inicio)) {
            throw new HorarioNoDisponibleException(
                    "La hora de fin debe ser posterior a la hora de inicio"
            );
        }
    }
}