package com.canchapro.ms_pagos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de la reserva es obligatorio")
    @Positive(message = "El ID de la reserva debe ser mayor que cero")
    @Column(nullable = false)
    private Long reservaId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor que cero")
    @Column(nullable = false)
    private Integer monto;

    @NotNull(message = "El método de pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @NotNull(message = "El estado del pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;

    @Column(nullable = false)
    private LocalDateTime fechaPago;
}