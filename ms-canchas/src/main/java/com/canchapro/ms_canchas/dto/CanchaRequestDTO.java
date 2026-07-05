package com.canchapro.ms_canchas.dto;

import com.canchapro.ms_canchas.entity.EstadoCancha;
import com.canchapro.ms_canchas.entity.TipoCancha;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CanchaRequestDTO {

    @NotBlank(message = "El nombre de la cancha es obligatorio")
    @Size(min = 3, max = 100,
            message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El tipo de cancha es obligatorio")
    private TipoCancha tipo;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(min = 3, max = 150,
            message = "La ubicación debe tener entre 3 y 150 caracteres")
    private String ubicacion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private Integer precio;

    @NotNull(message = "El estado de la cancha es obligatorio")
    private EstadoCancha estado;
}