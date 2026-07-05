package com.canchapro.ms_pagos.dto;

import com.canchapro.ms_pagos.entity.EstadoPago;
import com.canchapro.ms_pagos.entity.MetodoPago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponseDTO {

    private Long id;
    private Long reservaId;
    private Integer monto;
    private MetodoPago metodoPago;
    private EstadoPago estado;
    private LocalDateTime fechaPago;
}