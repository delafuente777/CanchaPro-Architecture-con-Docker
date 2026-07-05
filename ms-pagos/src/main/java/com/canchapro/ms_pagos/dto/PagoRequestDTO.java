package com.canchapro.ms_pagos.dto;

import com.canchapro.ms_pagos.entity.MetodoPago;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequestDTO {

    @NotNull(message = "El ID de la reserva es obligatorio")
    @Positive(message = "El ID de la reserva debe ser mayor que cero")
    private Long reservaId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor que cero")
    private Integer monto;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;
}