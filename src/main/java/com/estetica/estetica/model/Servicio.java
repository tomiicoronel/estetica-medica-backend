package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "servicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Servicio", description = "Entidad JPA que representa un servicio ofrecido por una profesional.")
public class Servicio {

    @Schema(description = "Identificador único UUID del servicio", example = "750e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Schema(description = "Profesional dueña del servicio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;

    @Schema(description = "Nombre del servicio", example = "Limpieza facial profunda")
    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Schema(description = "Descripción detallada del servicio")
    @Column(name = "descripcion", columnDefinition = "TEXT")
    @NotBlank(message = "La descripcion del servicio es obligatorio")
    private String descripcion;

    @Schema(description = "Precio actual del servicio", example = "15000.00")
    @NotNull(message = "El precio es obligatorio")
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Schema(description = "Indica si el servicio está activo y disponible", example = "true")
    @NotNull(message = "El campo activo es obligatorio")
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Schema(description = "Fecha y hora de creación")
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización")
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
        this.actualizadoEn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }
}
