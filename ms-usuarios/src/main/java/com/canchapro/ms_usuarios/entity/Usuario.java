package com.canchapro.ms_usuarios.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^[0-9]{8,15}$",
            message = "El teléfono debe contener entre 8 y 15 dígitos"
    )
    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private Boolean activo;
}