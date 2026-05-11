package com.estetica.estetica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historias_clinicas_corporales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "HistoriaClinicaCorporal", description = "Entidad JPA que representa la ficha clínica corporal de un paciente.")
public class HistoriaClinicaCorporal {

    @Schema(description = "Identificador único UUID de la ficha clínica corporal", example = "960e8400-e29b-41d4-a716-446655440000")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Schema(description = "Paciente al que pertenece esta ficha clínica corporal")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @Schema(description = "Indica antecedente de hipertensión arterial", example = "false")
    @Column(name = "hta") private Boolean hta;
    @Schema(description = "Indica antecedente de diabetes", example = "false")
    @Column(name = "dbt") private Boolean dbt;
    @Schema(description = "Indica antecedente de hipotiroidismo", example = "false")
    @Column(name = "hipotiroidismo") private Boolean hipotiroidismo;
    @Schema(description = "Indica antecedente de hipertiroidismo", example = "false")
    @Column(name = "hipertiroidismo") private Boolean hipertiroidismo;
    @Schema(description = "Indica antecedente de anemia", example = "false")
    @Column(name = "anemia") private Boolean anemia;
    @Schema(description = "Indica antecedente de enfermedades autoinmunes", example = "false")
    @Column(name = "enfermedades_autoinmunes") private Boolean enfermedadesAutoinmunes;
    @Schema(description = "Indica antecedente de glaucoma", example = "false")
    @Column(name = "glaucoma") private Boolean glaucoma;
    @Schema(description = "Indica antecedente de enfermedad neuromuscular", example = "false")
    @Column(name = "enfermedad_neuromuscular") private Boolean enfermedadNeuromuscular;
    @Schema(description = "Indica antecedente de trastornos de coagulación", example = "false")
    @Column(name = "trastornos_coagulacion") private Boolean trastornosCoagulacion;
    @Schema(description = "Indica antecedente de alteración de cicatrización", example = "false")
    @Column(name = "alteracion_cicatrizacion") private Boolean alteracionCicatrizacion;
    @Schema(description = "Indica si la paciente tiene marcapasos", example = "false")
    @Column(name = "marcapasos") private Boolean marcapasos;
    @Schema(description = "Indica si la paciente tiene prótesis metálica", example = "false")
    @Column(name = "protesis_metalica") private Boolean protesisMetalica;
    @Schema(description = "Indica antecedente oncológico", example = "false")
    @Column(name = "cancer") private Boolean cancer;

    @Schema(description = "Texto libre para otros antecedentes patológicos no contemplados")
    @Column(name = "otro_antecedente_patologico", columnDefinition = "TEXT")
    private String otroAntecedentePatologico;

    @Schema(description = "Indica tabaquismo", example = "false")
    @Column(name = "tbq") private Boolean tbq;
    @Schema(description = "Indica consumo de alcohol", example = "false")
    @Column(name = "alcohol") private Boolean alcohol;

    @Schema(description = "Texto libre para otros antecedentes tóxicos")
    @Column(name = "otras_toxico", columnDefinition = "TEXT")
    private String otrasToxico;

    @Schema(description = "Indica alergia al huevo", example = "false")
    @Column(name = "alergico_huevo") private Boolean alergicoHuevo;
    @Schema(description = "Indica alergia a la anestesia", example = "false")
    @Column(name = "alergico_anestesia") private Boolean alergicoAnestesia;
    @Schema(description = "Indica alergia al pescado", example = "false")
    @Column(name = "alergico_fish") private Boolean alergicoFish;

    @Schema(description = "Texto libre para otras alergias")
    @Column(name = "otras_alergias", columnDefinition = "TEXT")
    private String otrasAlergias;

    @Schema(description = "Antecedentes quirúrgicos relevantes")
    @Column(name = "antecedentes_quirurgicos", columnDefinition = "TEXT")
    private String antecedentesQuirurgicos;

    @Schema(description = "Fecha de última menstruación u observación ginecológica", example = "Sin datos")
    @Column(name = "fum")
    private String fum;

    @Schema(description = "Indica embarazo actual", example = "false")
    @Column(name = "embarazo") private Boolean embarazo;
    @Schema(description = "Indica lactancia actual", example = "false")
    @Column(name = "lactancia") private Boolean lactancia;
    @Schema(description = "Indica antecedente o presencia de herpes labial", example = "false")
    @Column(name = "herpes_labial") private Boolean herpesLabial;

    @Schema(description = "Medicación habitual indicada por la paciente")
    @Column(name = "medicacion_habitual", columnDefinition = "TEXT")
    private String medicacionHabitual;

    @Schema(description = "Indica si tomó aspirina durante la última semana", example = "false")
    @Column(name = "aspirina_semana") private Boolean aspirinaSemana;
    @Schema(description = "Indica si mantiene una alimentación saludable", example = "true")
    @Column(name = "alimentacion_saludable") private Boolean alimentacionSaludable;
    @Schema(description = "Indica si bebe agua regularmente", example = "true")
    @Column(name = "bebe_agua") private Boolean bebeAgua;

    @Schema(description = "Detalle sobre sedentarismo, actividad física o gimnasia")
    @Column(name = "sedentarismo_gimnasia", columnDefinition = "TEXT")
    private String sedentarismoGimnasia;

    @Schema(description = "Indica ortostatismo prolongado", example = "false")
    @Column(name = "ortostatismo_prolongado") private Boolean ortostatismoProlongado;
    @Schema(description = "Indica uso de medias de compresión", example = "false")
    @Column(name = "medias_compresion") private Boolean mediasCompresion;
    @Schema(description = "Indica si realizó tratamientos corporales previos", example = "false")
    @Column(name = "tratamientos_previos") private Boolean tratamientosPrevios;

    @Schema(description = "Detalle de tratamientos corporales previos")
    @Column(name = "tratamientos_previos_cuales", columnDefinition = "TEXT")
    private String tratamientosPreviosCuales;

    @Schema(description = "Fecha o período en el que realizó tratamientos previos")
    @Column(name = "tratamientos_previos_cuando", columnDefinition = "TEXT")
    private String tratamientosPreviosCuando;

    @Schema(description = "Respuesta obtenida frente a tratamientos previos")
    @Column(name = "tratamientos_previos_respuesta", columnDefinition = "TEXT")
    private String tratamientosPreviosRespuesta;

    @Schema(description = "Indica si realizará un viaje durante el próximo mes", example = "false")
    @Column(name = "viaje_proximo_mes") private Boolean viajeProximoMes;

    @Schema(description = "Presencia de otros materiales observados durante el examen")
    @Column(name = "presencia_otros_materiales", columnDefinition = "TEXT")
    private String presenciaOtrosMateriales;

    @Schema(description = "Secuelas observadas por tratamientos previos")
    @Column(name = "secuelas_tratamientos_previos", columnDefinition = "TEXT")
    private String secuelasTratamientosPrevios;

    @Schema(description = "Indica presencia de arañas vasculares", example = "false")
    @Column(name = "aranas_vasculares") private Boolean aranasVasculares;
    @Schema(description = "Indica presencia de telangiectasias", example = "false")
    @Column(name = "telangiectasias") private Boolean telangiectasias;
    @Schema(description = "Indica presencia de várices", example = "false")
    @Column(name = "varices") private Boolean varices;
    @Schema(description = "Indica presencia de celulitis", example = "true")
    @Column(name = "celulitis") private Boolean celulitis;
    @Schema(description = "Indica presencia de flacidez", example = "true")
    @Column(name = "flacidez") private Boolean flacidez;
    @Schema(description = "Indica presencia de estrías", example = "false")
    @Column(name = "estrias") private Boolean estrias;

    @Schema(description = "Detalle de adiposidad localizada")
    @Column(name = "adiposidad_localizada", columnDefinition = "TEXT")
    private String adiposidadLocalizada;

    @Schema(description = "Peso actual de la paciente en kilogramos", example = "64.50")
    @Column(name = "peso_actual", precision = 6, scale = 2)
    private BigDecimal pesoActual;

    @Schema(description = "Peso habitual de la paciente en kilogramos", example = "62.00")
    @Column(name = "peso_habitual", precision = 6, scale = 2)
    private BigDecimal pesoHabitual;

    @Schema(description = "Índice de masa corporal", example = "23.70")
    @Column(name = "imc", precision = 5, scale = 2)
    private BigDecimal imc;

    @Schema(description = "Perímetro de cintura en centímetros", example = "78.50")
    @Column(name = "perimetro_cintura", precision = 5, scale = 2)
    private BigDecimal perimetroCintura;

    @Schema(description = "Indica si se tomó fotografía para seguimiento", example = "true")
    @Column(name = "se_toma_fotografia") private Boolean seTomaFotografia;

    @Schema(description = "Diagnóstico clínico y tratamiento propuesto o realizado")
    @Column(name = "diagnostico_y_tratamiento", columnDefinition = "TEXT")
    private String diagnosticoYTratamiento;

    @Schema(description = "Observaciones posteriores al tratamiento o controles")
    @Column(name = "observaciones_posteriores", columnDefinition = "TEXT")
    private String observacionesPosteriores;

    @Schema(description = "Fecha y hora en la que se creó la ficha corporal", example = "2026-05-07T18:30:00")
    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de la última modificación de la ficha corporal", example = "2026-05-07T19:15:00")
    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
