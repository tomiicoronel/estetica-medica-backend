package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "profesionales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Profesional", description = "Entidad JPA que representa a una profesional del sistema. Cada profesional funciona como tenant aislado y administra sus propios pacientes, servicios y turnos.")
public class Profesional {

    @Schema(description = "Identificador único UUID de la profesional", example = "550e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Schema(description = "Nombre de la profesional", example = "María")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Schema(description = "Apellido de la profesional", example = "González")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Schema(description = "Email único de la profesional. En el futuro puede funcionar como identificador de login", example = "maria.gonzalez@email.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Schema(description = "Teléfono de contacto de la profesional", example = "1123456789")
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Schema(description = "Especialidad principal de la profesional", example = "Cosmetología")
    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    @Column(name = "especialidad", length = 100)
    private String especialidad;

    @Schema(description = "Servicios creados por la profesional")
    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Servicio> servicios = new ArrayList<>();

    @Schema(description = "Pacientes pertenecientes a la profesional")
    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Paciente> pacientes = new ArrayList<>();

    @Schema(description = "Fecha y hora en la que se creó el registro", example = "2026-05-07T18:30:00")
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de la última modificación del registro", example = "2026-05-07T19:15:00")
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
