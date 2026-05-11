package com.estetica.estetica.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ValidationErrorResponse", description = "Respuesta estándar para errores de validación producidos por @Valid.")
public class ValidationErrorResponse {

    @Schema(description = "Fecha y hora en la que ocurrió el error", example = "2026-05-07T18:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código HTTP devuelto por la API", example = "400")
    private Integer status;

    @Schema(description = "Descripción corta del tipo de error", example = "Error de validación")
    private String error;

    @Schema(description = "Mapa donde la clave es el campo inválido y el valor es el mensaje de validación")
    private Map<String, String> mensajes;
}

