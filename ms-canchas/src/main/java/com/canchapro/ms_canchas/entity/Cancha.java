package com.canchapro.ms_canchas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "canchas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la cancha es obligatorio")
    @Size(min = 3, max = 100,
            message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotNull(message = "El tipo de cancha es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCancha tipo;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(min = 3, max = 150,
            message = "La ubicación debe tener entre 3 y 150 caracteres")
    @Column(nullable = false)
    private String ubicacion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    @Column(nullable = false)
    private Integer precio;

    @NotNull(message = "El estado de la cancha es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCancha estado;
}