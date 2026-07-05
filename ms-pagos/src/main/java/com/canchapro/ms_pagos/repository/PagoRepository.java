package com.canchapro.ms_pagos.repository;

import com.canchapro.ms_pagos.entity.EstadoPago;
import com.canchapro.ms_pagos.entity.Pago;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByReservaId(Long reservaId);

    Optional<Pago> findFirstByReservaIdOrderByFechaPagoDesc(Long reservaId);

    List<Pago> findByEstado(EstadoPago estado);

    boolean existsByReservaIdAndEstado(Long reservaId, EstadoPago estado);
}