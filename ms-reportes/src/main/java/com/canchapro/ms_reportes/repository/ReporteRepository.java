package com.canchapro.ms_reportes.repository;

import com.canchapro.ms_reportes.entity.EstadoReporte;
import com.canchapro.ms_reportes.entity.Reporte;
import com.canchapro.ms_reportes.entity.TipoReporte;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByTipo(TipoReporte tipo);

    List<Reporte> findByEstado(EstadoReporte estado);

    List<Reporte> findByFechaGeneracionBetween(
            LocalDateTime inicio,
            LocalDateTime fin
    );
}