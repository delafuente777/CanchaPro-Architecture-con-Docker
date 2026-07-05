package com.canchapro.ms_usuarios.dto;

import com.canchapro.ms_usuarios.entity.Role;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String correo;
    private Role role;
    private String telefono;
    private Boolean activo;
}