package com.canchapro.ms_pagos.util;

import com.canchapro.ms_pagos.dto.PagoResponseDTO;
import com.canchapro.ms_pagos.entity.Pago;

import java.util.Objects;

public class PagoMapper {

    private PagoMapper() {
    }

    public static PagoResponseDTO toResponseDTO(
            Pago pago
    ) {

        Pago pagoValidado = Objects.requireNonNull(
                pago,
                "El pago no puede ser null"
        );

        return PagoResponseDTO.builder()
                .id(pagoValidado.getId())
                .reservaId(pagoValidado.getReservaId())
                .monto(pagoValidado.getMonto())
                .metodoPago(pagoValidado.getMetodoPago())
                .estado(pagoValidado.getEstado())
                .fechaPago(pagoValidado.getFechaPago())
                .build();
    }
}