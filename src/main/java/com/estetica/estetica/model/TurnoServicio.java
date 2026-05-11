package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "turno_servicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "TurnoServicio", description = "Entidad intermedia que vincula un turno con un servicio y conserva el precio del servicio al momento de crear el turno.")
public class TurnoServicio {

    @Schema(description = "Identificador único UUID", example = "870e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Schema(description = "Turno al que pertenece esta línea")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;

    @Schema(description = "Servicio incluido en el turno")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Schema(description = "Precio congelado del servicio al momento de crear el turno", example = "15000.00")
    @Column(name = "precio_momento", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioMomento;
}
