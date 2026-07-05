package com.canchapro.ms_canchas.repository;

import com.canchapro.ms_canchas.entity.Cancha;
import com.canchapro.ms_canchas.entity.EstadoCancha;
import com.canchapro.ms_canchas.entity.TipoCancha;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CanchaRepository extends JpaRepository<Cancha, Long> {

    boolean existsByNombreAndUbicacion(
            String nombre,
            String ubicacion
    );

    Optional<Cancha> findByNombreAndUbicacion(
            String nombre,
            String ubicacion
    );

    List<Cancha> findByEstado(
            EstadoCancha estado
    );

    List<Cancha> findByTipo(
            TipoCancha tipo
    );
}