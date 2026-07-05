package com.canchapro.ms_pagos.dto;

import com.canchapro.ms_pagos.entity.EstadoPago;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CambiarEstadoPagoDTO {

    @NotNull(message = "El estado del pago es obligatorio")
    private EstadoPago estado;
}