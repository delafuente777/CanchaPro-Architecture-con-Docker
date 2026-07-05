package com.canchapro.ms_canchas.service;

import com.canchapro.ms_canchas.dto.CanchaRequestDTO;
import com.canchapro.ms_canchas.dto.CanchaResponseDTO;
import com.canchapro.ms_canchas.entity.Cancha;
import com.canchapro.ms_canchas.entity.EstadoCancha;
import com.canchapro.ms_canchas.entity.TipoCancha;
import com.canchapro.ms_canchas.exception.CanchaNoDisponibleException;
import com.canchapro.ms_canchas.exception.CanchaNoEncontradaException;
import com.canchapro.ms_canchas.exception.CanchaYaExisteException;
import com.canchapro.ms_canchas.repository.CanchaRepository;
import com.canchapro.ms_canchas.util.CanchaMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CanchaService {

    private final CanchaRepository repository;

    public List<CanchaResponseDTO> listarTodas() {

        log.info("Listando todas las canchas");

        return repository.findAll()
                .stream()
                .map(CanchaMapper::toResponseDTO)
                .toList();
    }

    public CanchaResponseDTO buscarPorId(Long id) {

        Long canchaId = Objects.requireNonNull(
                id,
                "El ID de la cancha no puede ser null"
        );

        log.info("Buscando cancha con ID {}", canchaId);

        Cancha cancha = repository.findById(canchaId)
                .orElseThrow(
                        () -> new CanchaNoEncontradaException(
                                "No existe cancha con ID: " + canchaId
                        )
                );

        return CanchaMapper.toResponseDTO(cancha);
    }

    public List<CanchaResponseDTO> listarPorEstado(
            EstadoCancha estado
    ) {

        EstadoCancha estadoValidado = Objects.requireNonNull(
                estado,
                "El estado de la cancha no puede ser null"
        );

        log.info("Listando canchas por estado {}", estadoValidado);

        return repository.findByEstado(estadoValidado)
                .stream()
                .map(CanchaMapper::toResponseDTO)
                .toList();
    }

    public List<CanchaResponseDTO> listarPorTipo(
            TipoCancha tipo
    ) {

        TipoCancha tipoValidado = Objects.requireNonNull(
                tipo,
                "El tipo de cancha no puede ser null"
        );

        log.info("Listando canchas por tipo {}", tipoValidado);

        return repository.findByTipo(tipoValidado)
                .stream()
                .map(CanchaMapper::toResponseDTO)
                .toList();
    }

    public CanchaResponseDTO crear(
            CanchaRequestDTO request
    ) {

        CanchaRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de cancha no puede ser null"
        );

        log.info(
                "Creando cancha {} en {}",
                requestValidado.getNombre(),
                requestValidado.getUbicacion()
        );

        if (repository.existsByNombreAndUbicacion(
                requestValidado.getNombre(),
                requestValidado.getUbicacion()
        )) {
            throw new CanchaYaExisteException(
                    "Ya existe una cancha con el nombre "
                            + requestValidado.getNombre()
                            + " en la ubicación "
                            + requestValidado.getUbicacion()
            );
        }

        Cancha cancha = Cancha.builder()
                .nombre(requestValidado.getNombre())
                .tipo(requestValidado.getTipo())
                .ubicacion(requestValidado.getUbicacion())
                .precio(requestValidado.getPrecio())
                .estado(requestValidado.getEstado())
                .build();

        Cancha guardada = repository.save(
                Objects.requireNonNull(
                        cancha,
                        "La cancha no puede ser null"
                )
        );

        log.info(
                "Cancha creada correctamente con ID {}",
                guardada.getId()
        );

        return CanchaMapper.toResponseDTO(guardada);
    }

    public CanchaResponseDTO actualizar(
            Long id,
            CanchaRequestDTO request
    ) {

        Long canchaId = Objects.requireNonNull(
                id,
                "El ID de la cancha no puede ser null"
        );

        CanchaRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de cancha no puede ser null"
        );

        log.info("Actualizando cancha con ID {}", canchaId);

        Cancha cancha = repository.findById(canchaId)
                .orElseThrow(
                        () -> new CanchaNoEncontradaException(
                                "No existe cancha con ID: " + canchaId
                        )
                );

        repository.findByNombreAndUbicacion(
                        requestValidado.getNombre(),
                        requestValidado.getUbicacion()
                )
                .filter(canchaExistente ->
                        !canchaExistente.getId().equals(canchaId)
                )
                .ifPresent(canchaExistente -> {
                    throw new CanchaYaExisteException(
                            "Ya existe otra cancha con el nombre "
                                    + requestValidado.getNombre()
                                    + " en la ubicación "
                                    + requestValidado.getUbicacion()
                    );
                });

        cancha.setNombre(requestValidado.getNombre());
        cancha.setTipo(requestValidado.getTipo());
        cancha.setUbicacion(requestValidado.getUbicacion());
        cancha.setPrecio(requestValidado.getPrecio());
        cancha.setEstado(requestValidado.getEstado());

        Cancha actualizada = repository.save(
                Objects.requireNonNull(
                        cancha,
                        "La cancha no puede ser null"
                )
        );

        log.info(
                "Cancha actualizada correctamente con ID {}",
                actualizada.getId()
        );

        return CanchaMapper.toResponseDTO(actualizada);
    }

    public void eliminar(Long id) {

        Long canchaId = Objects.requireNonNull(
                id,
                "El ID de la cancha no puede ser null"
        );

        log.info("Eliminando cancha con ID {}", canchaId);

        Cancha cancha = repository.findById(canchaId)
                .orElseThrow(
                        () -> new CanchaNoEncontradaException(
                                "No existe cancha con ID: " + canchaId
                        )
                );

        repository.delete(
                Objects.requireNonNull(
                        cancha,
                        "La cancha no puede ser null"
                )
        );

        log.info(
                "Cancha eliminada correctamente con ID {}",
                canchaId
        );
    }

    public CanchaResponseDTO cambiarEstado(
            Long id,
            EstadoCancha estado
    ) {

        Long canchaId = Objects.requireNonNull(
                id,
                "El ID de la cancha no puede ser null"
        );

        EstadoCancha estadoValidado = Objects.requireNonNull(
                estado,
                "El estado no puede ser null"
        );

        log.info(
                "Cambiando estado de cancha ID {} a {}",
                canchaId,
                estadoValidado
        );

        Cancha cancha = repository.findById(canchaId)
                .orElseThrow(
                        () -> new CanchaNoEncontradaException(
                                "No existe cancha con ID: " + canchaId
                        )
                );

        cancha.setEstado(estadoValidado);

        Cancha actualizada = repository.save(cancha);

        return CanchaMapper.toResponseDTO(actualizada);
    }

    public void validarCanchaDisponible(Long id) {

        Long canchaId = Objects.requireNonNull(
                id,
                "El ID de la cancha no puede ser null"
        );

        log.info("Validando disponibilidad de cancha ID {}", canchaId);

        Cancha cancha = repository.findById(canchaId)
                .orElseThrow(
                        () -> new CanchaNoEncontradaException(
                                "No existe cancha con ID: " + canchaId
                        )
                );

        if (!EstadoCancha.DISPONIBLE.equals(cancha.getEstado())) {
            throw new CanchaNoDisponibleException(
                    "La cancha con ID "
                            + canchaId
                            + " no está disponible"
            );
        }
    }
}