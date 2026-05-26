package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RolUsuario", description = "Roles de usuario soportados por el sistema.")
public enum RolUsuario {
    PROFESIONAL,
    PACIENTE
}

