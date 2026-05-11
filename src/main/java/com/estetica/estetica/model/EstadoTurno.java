package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados posibles de un turno")
public enum EstadoTurno {
    PENDIENTE,
    CONFIRMADO,
    REALIZADO,
    CANCELADO
}
