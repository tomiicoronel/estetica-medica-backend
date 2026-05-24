package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bloqueos_agenda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "BloqueoAgenda", description = "Entidad JPA que representa un rango half-open [fechaInicio, fechaFin) en el que una profesional no está disponible para recibir turnos.")
public class BloqueoAgenda {

    @Schema(description = "Identificador único UUID del bloqueo de agenda", example = "a50e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Schema(description = "Profesional dueña del bloqueo de agenda")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;

    @Schema(description = "Fecha y hora de inicio del bloqueo, incluida en el rango", example = "2026-06-10T14:00:00")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha y hora de fin del bloqueo, excluida del rango. A partir de este horario la agenda vuelve a estar disponible", example = "2026-06-10T16:00:00")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Schema(description = "Motivo opcional del bloqueo", example = "Capacitación fuera del consultorio")
    @Column(name = "motivo", length = 200)
    private String motivo;

    @Schema(description = "Fecha y hora de creación")
    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización")
    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}

