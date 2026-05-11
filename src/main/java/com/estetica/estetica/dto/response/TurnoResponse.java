package com.estetica.estetica.dto.response;

import com.estetica.estetica.model.EstadoTurno;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "TurnoResponse", description = "Datos devueltos por la API para un turno, incluyendo servicios con precio congelado.")
public class TurnoResponse {

    @Schema(description = "Identificador único UUID del turno", example = "850e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "UUID de la profesional dueña del turno", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID profesionalId;

    @Schema(description = "UUID del paciente del turno", example = "650e8400-e29b-41d4-a716-446655440000")
    private UUID pacienteId;

    @Schema(description = "Fecha y hora agendada", example = "2026-05-10T15:30:00")
    private LocalDateTime fechaHora;

    @Schema(description = "Estado actual del turno", example = "PENDIENTE", allowableValues = {"PENDIENTE", "CONFIRMADO", "REALIZADO", "CANCELADO"})
    private EstadoTurno estado;

    @Schema(description = "Monto total calculado sumando los precios congelados", example = "28000.00")
    private BigDecimal montoTotal;

    @Schema(description = "Notas opcionales sobre el turno", example = "Primera sesión")
    private String observaciones;

    @ArraySchema(schema = @Schema(implementation = TurnoServicioResponse.class))
    private List<TurnoServicioResponse> servicios;

    @Schema(description = "Fecha y hora de creación", example = "2026-04-24T10:30:00")
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización", example = "2026-04-24T10:30:00")
    private LocalDateTime actualizadoEn;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "TurnoServicioResponse", description = "Servicio incluido en un turno con precio congelado.")
    public static class TurnoServicioResponse {
        @Schema(description = "UUID del servicio", example = "750e8400-e29b-41d4-a716-446655440000")
        private UUID servicioId;

        @Schema(description = "Nombre del servicio", example = "Limpieza facial profunda")
        private String nombre;

        @Schema(description = "Precio congelado al momento de crear el turno", example = "15000.00")
        private BigDecimal precioMomento;
    }
}
