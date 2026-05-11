package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "turnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Turno", description = "Entidad JPA que representa un turno agendado para un paciente con uno o más servicios.")
public class Turno {

    @Schema(description = "Identificador único UUID del turno", example = "850e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Schema(description = "Profesional dueña del turno")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;

    @Schema(description = "Paciente asignado al turno")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Schema(description = "Fecha y hora del turno", example = "2026-05-10T15:30:00")
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Schema(description = "Estado actual del turno", example = "PENDIENTE", allowableValues = {"PENDIENTE", "CONFIRMADO", "REALIZADO", "CANCELADO"})
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoTurno estado = EstadoTurno.PENDIENTE;

    @Schema(description = "Monto total calculado sumando los precios congelados", example = "28000.00")
    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoTotal;

    @Schema(description = "Notas opcionales del turno")
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Schema(description = "Servicios incluidos en el turno con precio congelado")
    @Builder.Default
    @OneToMany(mappedBy = "turno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TurnoServicio> turnoServicios = new ArrayList<>();

    @Schema(description = "Fecha y hora de creación")
    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización")
    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
