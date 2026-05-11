package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Paciente", description = "Entidad JPA que representa un paciente perteneciente a una profesional.")
public class Paciente {

    @Schema(description = "Identificador único UUID del paciente")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Schema(description = "Profesional dueña del paciente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;

    @Schema(description = "Nombre del paciente")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Schema(description = "Apellido del paciente")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Schema(description = "DNI o CUIT del paciente")
    @NotBlank(message = "El DNI/CUIT es obligatorio")
    @Size(max = 20, message = "El DNI/CUIT no puede superar los 20 caracteres")
    @Column(name = "dni_cuit", nullable = false, length = 20)
    private String dniCuit;

    @Schema(description = "Fecha de nacimiento")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Schema(description = "Teléfono de contacto")
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Schema(description = "Email del paciente")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    @Column(name = "email", length = 150)
    private String email;

    @Schema(description = "Profesión u ocupación")
    @Size(max = 100, message = "La profesión no puede superar los 100 caracteres")
    @Column(name = "profesion", length = 100)
    private String profesion;

    @Schema(description = "Domicilio")
    @Size(max = 255, message = "El domicilio no puede superar los 255 caracteres")
    @Column(name = "domicilio")
    private String domicilio;

    @Schema(description = "Obra social")
    @Size(max = 100, message = "La obra social no puede superar los 100 caracteres")
    @Column(name = "obra_social", length = 100)
    private String obraSocial;

    @Schema(description = "Número de afiliado de la obra social")
    @Size(max = 50, message = "El número de obra social no puede superar los 50 caracteres")
    @Column(name = "numero_obra_social", length = 50)
    private String numeroObraSocial;

    @Schema(description = "Nombre del contacto de emergencia")
    @Size(max = 100, message = "El nombre del contacto de emergencia no puede superar los 100 caracteres")
    @Column(name = "contacto_emergencia_nombre", length = 100)
    private String contactoEmergenciaNombre;

    @Schema(description = "Teléfono del contacto de emergencia")
    @Size(max = 20, message = "El teléfono del contacto de emergencia no puede superar los 20 caracteres")
    @Column(name = "contacto_emergencia_telefono", length = 20)
    private String contactoEmergenciaTelefono;

    @Schema(description = "Parentesco del contacto de emergencia")
    @Size(max = 50, message = "El parentesco no puede superar los 50 caracteres")
    @Column(name = "contacto_emergencia_parentesco", length = 50)
    private String contactoEmergenciaParentesco;

    @Schema(description = "Primera entidad de traslado")
    @Size(max = 100, message = "La entidad de traslado no puede superar los 100 caracteres")
    @Column(name = "entidad_traslado_1", length = 100)
    private String entidadTraslado1;

    @Schema(description = "Segunda entidad de traslado")
    @Size(max = 100, message = "La entidad de traslado no puede superar los 100 caracteres")
    @Column(name = "entidad_traslado_2", length = 100)
    private String entidadTraslado2;

    @Schema(description = "Indica si el paciente está activo o archivado")
    @Builder.Default
    @Column(name = "activo", nullable = false)
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
