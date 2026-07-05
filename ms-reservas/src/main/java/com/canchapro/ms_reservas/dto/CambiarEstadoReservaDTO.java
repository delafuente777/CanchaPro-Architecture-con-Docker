package com.canchapro.ms_reservas.dto;

import com.canchapro.ms_reservas.entity.EstadoReserva;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CambiarEstadoReservaDTO {

    @NotNull(message = "El estado de la reserva es obligatorio")
    private EstadoReserva estado;
}