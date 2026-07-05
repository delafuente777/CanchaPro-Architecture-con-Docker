package com.canchapro.ms_reservas.service;

import com.canchapro.ms_reservas.client.CanchaClient;
import com.canchapro.ms_reservas.client.DisponibilidadClient;
import com.canchapro.ms_reservas.client.PagoClient;
import com.canchapro.ms_reservas.client.UsuarioClient;
import com.canchapro.ms_reservas.dto.CambiarEstadoReservaDTO;
import com.canchapro.ms_reservas.dto.ReservaRequestDTO;
import com.canchapro.ms_reservas.dto.ReservaResponseDTO;
import com.canchapro.ms_reservas.entity.EstadoReserva;
import com.canchapro.ms_reservas.entity.Reserva;
import com.canchapro.ms_reservas.exception.MicroservicioException;
import com.canchapro.ms_reservas.exception.ReservaException;
import com.canchapro.ms_reservas.exception.ReservaNoEncontradaException;
import com.canchapro.ms_reservas.mapper.ReservaMapper;
import com.canchapro.ms_reservas.repository.ReservaRepository;

import feign.FeignException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservaService {

    private static final List<EstadoReserva> ESTADOS_ACTIVOS = List.of(
            EstadoReserva.PENDIENTE,
            EstadoReserva.CONFIRMADA,
            EstadoReserva.PAGADA
    );

    private final ReservaRepository repository;
    private final UsuarioClient usuarioClient;
    private final CanchaClient canchaClient;
    private final DisponibilidadClient disponibilidadClient;
    private final PagoClient pagoClient;

    public List<ReservaResponseDTO> listarTodas() {

        log.info("Listando todas las reservas");

        return repository.findAll()
                .stream()
                .map(ReservaMapper::toResponseDTO)
                .toList();
    }

    public ReservaResponseDTO buscarPorId(Long id) {

        Long reservaId = Objects.requireNonNull(
                id,
                "El ID de la reserva no puede ser null"
        );

        Reserva reserva = repository.findById(reservaId)
                .orElseThrow(
                        () -> new ReservaNoEncontradaException(
                                "No existe reserva con ID: " + reservaId
                        )
                );

        return ReservaMapper.toResponseDTO(reserva);
    }

    public ReservaResponseDTO crear(
            ReservaRequestDTO request
    ) {

        ReservaRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de reserva no puede ser null"
        );

        validarHorario(
                requestValidado.getHoraInicio(),
                requestValidado.getHoraFin()
        );

        validarUsuario(requestValidado.getUsuarioId());
        validarCancha(requestValidado.getCanchaId());
        validarHorarioNoReservado(requestValidado);
        validarDisponibilidad(requestValidado);

        reservarHorarioEnDisponibilidad(requestValidado);

        Reserva reserva = Reserva.builder()
                .usuarioId(requestValidado.getUsuarioId())
                .canchaId(requestValidado.getCanchaId())
                .fecha(requestValidado.getFecha())
                .horaInicio(requestValidado.getHoraInicio())
                .horaFin(requestValidado.getHoraFin())
                .monto(requestValidado.getMonto())
                .estado(EstadoReserva.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .build();

        Reserva guardada = repository.save(reserva);

        log.info(
                "Reserva creada correctamente con ID {}",
                guardada.getId()
        );

        return ReservaMapper.toResponseDTO(guardada);
    }

    public ReservaResponseDTO actualizar(
            Long id,
            ReservaRequestDTO request
    ) {

        Long reservaId = Objects.requireNonNull(
                id,
                "El ID de la reserva no puede ser null"
        );

        ReservaRequestDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de reserva no puede ser null"
        );

        validarHorario(
                requestValidado.getHoraInicio(),
                requestValidado.getHoraFin()
        );

        Reserva reserva = repository.findById(reservaId)
                .orElseThrow(
                        () -> new ReservaNoEncontradaException(
                                "No existe reserva con ID: " + reservaId
                        )
                );

        if (EstadoReserva.CANCELADA.equals(reserva.getEstado())
                || EstadoReserva.FINALIZADA.equals(reserva.getEstado())) {
            throw new ReservaException(
                    "No se puede actualizar una reserva cancelada o finalizada"
            );
        }

        validarUsuario(requestValidado.getUsuarioId());
        validarCancha(requestValidado.getCanchaId());

        if (!reserva.getCanchaId().equals(requestValidado.getCanchaId())
                || !reserva.getFecha().equals(requestValidado.getFecha())
                || !reserva.getHoraInicio().equals(requestValidado.getHoraInicio())) {

            validarHorarioNoReservadoParaActualizar(
                    reservaId,
                    requestValidado
            );

            validarDisponibilidad(requestValidado);
        }

        reserva.setUsuarioId(requestValidado.getUsuarioId());
        reserva.setCanchaId(requestValidado.getCanchaId());
        reserva.setFecha(requestValidado.getFecha());
        reserva.setHoraInicio(requestValidado.getHoraInicio());
        reserva.setHoraFin(requestValidado.getHoraFin());
        reserva.setMonto(requestValidado.getMonto());

        Reserva actualizada = repository.save(reserva);

        log.info(
                "Reserva actualizada correctamente con ID {}",
                reservaId
        );

        return ReservaMapper.toResponseDTO(actualizada);
    }

    public void eliminar(Long id) {

        Long reservaId = Objects.requireNonNull(
                id,
                "El ID de la reserva no puede ser null"
        );

        if (!repository.existsById(reservaId)) {
            throw new ReservaNoEncontradaException(
                    "No existe reserva con ID: " + reservaId
            );
        }

        repository.deleteById(reservaId);

        log.info(
                "Reserva eliminada correctamente con ID {}",
                reservaId
        );
    }

    public ReservaResponseDTO cancelar(Long id) {

        Long reservaId = Objects.requireNonNull(
                id,
                "El ID de la reserva no puede ser null"
        );

        Reserva reserva = repository.findById(reservaId)
                .orElseThrow(
                        () -> new ReservaNoEncontradaException(
                                "No existe reserva con ID: " + reservaId
                        )
                );

        if (EstadoReserva.CANCELADA.equals(reserva.getEstado())) {
            throw new ReservaException(
                    "La reserva ya se encuentra cancelada"
            );
        }

        if (EstadoReserva.FINALIZADA.equals(reserva.getEstado())) {
            throw new ReservaException(
                    "No se puede cancelar una reserva finalizada"
            );
        }

        liberarHorarioEnDisponibilidad(reserva);

        reserva.setEstado(EstadoReserva.CANCELADA);

        Reserva actualizada = repository.save(reserva);

        log.info(
                "Reserva cancelada correctamente con ID {}",
                reservaId
        );

        return ReservaMapper.toResponseDTO(actualizada);
    }

    public ReservaResponseDTO cambiarEstado(
            Long id,
            CambiarEstadoReservaDTO request
    ) {

        Long reservaId = Objects.requireNonNull(
                id,
                "El ID de la reserva no puede ser null"
        );

        CambiarEstadoReservaDTO requestValidado = Objects.requireNonNull(
                request,
                "La solicitud de cambio de estado no puede ser null"
        );

        Reserva reserva = repository.findById(reservaId)
                .orElseThrow(
                        () -> new ReservaNoEncontradaException(
                                "No existe reserva con ID: " + reservaId
                        )
                );

        reserva.setEstado(requestValidado.getEstado());

        Reserva actualizada = repository.save(reserva);

        log.info(
                "Estado de reserva ID {} cambiado a {}",
                reservaId,
                requestValidado.getEstado()
        );

        return ReservaMapper.toResponseDTO(actualizada);
    }

    public ReservaResponseDTO confirmarPago(Long id) {

        Long reservaId = Objects.requireNonNull(
                id,
                "El ID de la reserva no puede ser null"
        );

        Reserva reserva = repository.findById(reservaId)
                .orElseThrow(
                        () -> new ReservaNoEncontradaException(
                                "No existe reserva con ID: " + reservaId
                        )
                );

        try {
            pagoClient.validarPago(reservaId);
        } catch (FeignException ex) {
            throw new MicroservicioException(
                    "No se pudo validar el pago de la reserva"
            );
        }

        reserva.setEstado(EstadoReserva.PAGADA);

        Reserva actualizada = repository.save(reserva);

        log.info(
                "Pago confirmado para reserva ID {}",
                reservaId
        );

        return ReservaMapper.toResponseDTO(actualizada);
    }

    public List<ReservaResponseDTO> listarPorUsuario(Long usuarioId) {

        Long usuarioIdValidado = Objects.requireNonNull(
                usuarioId,
                "El ID del usuario no puede ser null"
        );

        return repository.findByUsuarioId(usuarioIdValidado)
                .stream()
                .map(ReservaMapper::toResponseDTO)
                .toList();
    }

    public List<ReservaResponseDTO> listarPorCancha(Long canchaId) {

        Long canchaIdValidado = Objects.requireNonNull(
                canchaId,
                "El ID de la cancha no puede ser null"
        );

        return repository.findByCanchaId(canchaIdValidado)
                .stream()
                .map(ReservaMapper::toResponseDTO)
                .toList();
    }

    public List<ReservaResponseDTO> listarPorEstado(
            EstadoReserva estado
    ) {

        EstadoReserva estadoValidado = Objects.requireNonNull(
                estado,
                "El estado no puede ser null"
        );

        return repository.findByEstado(estadoValidado)
                .stream()
                .map(ReservaMapper::toResponseDTO)
                .toList();
    }

    public List<ReservaResponseDTO> listarPorCanchaYFecha(
            Long canchaId,
            LocalDate fecha
    ) {

        Long canchaIdValidado = Objects.requireNonNull(
                canchaId,
                "El ID de la cancha no puede ser null"
        );

        LocalDate fechaValidada = Objects.requireNonNull(
                fecha,
                "La fecha no puede ser null"
        );

        return repository.findByCanchaIdAndFecha(
                        canchaIdValidado,
                        fechaValidada
                )
                .stream()
                .map(ReservaMapper::toResponseDTO)
                .toList();
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

    private void validarDisponibilidad(
            ReservaRequestDTO request
    ) {

        try {
            Boolean disponible = disponibilidadClient.consultarDisponible(
                    request.getCanchaId(),
                    request.getFecha().toString(),
                    request.getHoraInicio().toString()
            );

            if (!Boolean.TRUE.equals(disponible)) {
                throw new ReservaException(
                        "La cancha no se encuentra disponible en el horario solicitado"
                );
            }

        } catch (FeignException ex) {
            throw new MicroservicioException(
                    "No se pudo validar la disponibilidad"
            );
        }
    }

    private void reservarHorarioEnDisponibilidad(
            ReservaRequestDTO request
    ) {

        try {
            disponibilidadClient.reservarHorario(
                    request.getCanchaId(),
                    request.getFecha().toString(),
                    request.getHoraInicio().toString()
            );
        } catch (FeignException ex) {
            throw new MicroservicioException(
                    "No se pudo reservar el horario en disponibilidad"
            );
        }
    }

    private void liberarHorarioEnDisponibilidad(
            Reserva reserva
    ) {

        try {
            disponibilidadClient.liberarHorario(
                    reserva.getCanchaId(),
                    reserva.getFecha().toString(),
                    reserva.getHoraInicio().toString()
            );
        } catch (FeignException ex) {
            log.warn(
                    "No se pudo liberar disponibilidad para reserva ID {}",
                    reserva.getId()
            );
        }
    }

    private void validarHorarioNoReservado(
            ReservaRequestDTO request
    ) {

        boolean existeReserva = repository
                .existsByCanchaIdAndFechaAndHoraInicioAndEstadoIn(
                        request.getCanchaId(),
                        request.getFecha(),
                        request.getHoraInicio(),
                        ESTADOS_ACTIVOS
                );

        if (existeReserva) {
            throw new ReservaException(
                    "Ya existe una reserva activa para esa cancha, fecha y hora"
            );
        }
    }

    private void validarHorarioNoReservadoParaActualizar(
            Long reservaId,
            ReservaRequestDTO request
    ) {

        List<Reserva> reservas = repository.findByCanchaIdAndFecha(
                request.getCanchaId(),
                request.getFecha()
        );

        boolean existeOtraReserva = reservas.stream()
                .anyMatch(reserva ->
                        !reserva.getId().equals(reservaId)
                                && reserva.getHoraInicio().equals(
                                        request.getHoraInicio()
                                )
                                && ESTADOS_ACTIVOS.contains(
                                        reserva.getEstado()
                                )
                );

        if (existeOtraReserva) {
            throw new ReservaException(
                    "Ya existe otra reserva activa para esa cancha, fecha y hora"
            );
        }
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
            throw new ReservaException(
                    "La hora de fin debe ser posterior a la hora de inicio"
            );
        }
    }
}