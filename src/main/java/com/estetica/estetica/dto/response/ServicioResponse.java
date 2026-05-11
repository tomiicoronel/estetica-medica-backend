package com.estetica.estetica.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ServicioResponse", description = "Datos devueltos por la API para un servicio.")
public class ServicioResponse {

    @Schema(description = "Identificador único UUID del servicio", example = "750e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "UUID de la profesional dueña del servicio", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID profesionalId;

    @Schema(description = "Nombre del servicio", example = "Limpieza facial profunda")
    private String nombre;

    @Schema(description = "Descripción detallada del servicio", example = "Tratamiento de limpieza facial con extracción de comedones")
    private String descripcion;

    @Schema(description = "Precio actual del servicio", example = "15000.00")
    private BigDecimal precio;

    @Schema(description = "Indica si el servicio está activo y disponible", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha y hora de creación", example = "2026-04-24T10:30:00")
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización", example = "2026-04-24T10:30:00")
    private LocalDateTime actualizadoEn;
}
