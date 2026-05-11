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
@Schema(name = "HistoriaClinicaCorporalResponse", description = "Ficha clínica corporal devuelta por la API.")
public class HistoriaClinicaCorporalResponse {

    @Schema(description = "UUID de la ficha corporal") private UUID id;
    @Schema(description = "UUID del paciente dueño de la ficha") private UUID pacienteId;

    @Schema(description = "Hipertensión arterial") private Boolean hta;
    @Schema(description = "Diabetes") private Boolean dbt;
    @Schema(description = "Hipotiroidismo") private Boolean hipotiroidismo;
    @Schema(description = "Hipertiroidismo") private Boolean hipertiroidismo;
    @Schema(description = "Anemia") private Boolean anemia;
    @Schema(description = "Enfermedades autoinmunes") private Boolean enfermedadesAutoinmunes;
    @Schema(description = "Glaucoma") private Boolean glaucoma;
    @Schema(description = "Enfermedad neuromuscular") private Boolean enfermedadNeuromuscular;
    @Schema(description = "Trastornos de coagulación") private Boolean trastornosCoagulacion;
    @Schema(description = "Alteración de cicatrización") private Boolean alteracionCicatrizacion;
    @Schema(description = "Marcapasos") private Boolean marcapasos;
    @Schema(description = "Prótesis metálica") private Boolean protesisMetalica;
    @Schema(description = "Cáncer") private Boolean cancer;
    @Schema(description = "Otros antecedentes patológicos") private String otroAntecedentePatologico;

    @Schema(description = "Tabaquismo") private Boolean tbq;
    @Schema(description = "Alcohol") private Boolean alcohol;
    @Schema(description = "Otros antecedentes tóxicos") private String otrasToxico;

    @Schema(description = "Alergia al huevo") private Boolean alergicoHuevo;
    @Schema(description = "Alergia a anestesia") private Boolean alergicoAnestesia;
    @Schema(description = "Alergia a pescado") private Boolean alergicoFish;
    @Schema(description = "Otras alergias") private String otrasAlergias;

    @Schema(description = "Antecedentes quirúrgicos") private String antecedentesQuirurgicos;
    @Schema(description = "FUM") private String fum;
    @Schema(description = "Embarazo") private Boolean embarazo;
    @Schema(description = "Lactancia") private Boolean lactancia;

    @Schema(description = "Herpes labial") private Boolean herpesLabial;
    @Schema(description = "Medicación habitual") private String medicacionHabitual;
    @Schema(description = "Aspirina última semana") private Boolean aspirinaSemana;

    @Schema(description = "Alimentación saludable") private Boolean alimentacionSaludable;
    @Schema(description = "Bebe agua") private Boolean bebeAgua;
    @Schema(description = "Sedentarismo o actividad física") private String sedentarismoGimnasia;
    @Schema(description = "Ortostatismo prolongado") private Boolean ortostatismoProlongado;
    @Schema(description = "Medias de compresión") private Boolean mediasCompresion;
    @Schema(description = "Tratamientos previos") private Boolean tratamientosPrevios;
    @Schema(description = "Tratamientos previos realizados") private String tratamientosPreviosCuales;
    @Schema(description = "Cuándo se realizaron los tratamientos previos") private String tratamientosPreviosCuando;
    @Schema(description = "Respuesta a tratamientos previos") private String tratamientosPreviosRespuesta;
    @Schema(description = "Viaje próximo mes") private Boolean viajeProximoMes;

    @Schema(description = "Presencia de otros materiales") private String presenciaOtrosMateriales;
    @Schema(description = "Secuelas de tratamientos previos") private String secuelasTratamientosPrevios;
    @Schema(description = "Arañas vasculares") private Boolean aranasVasculares;
    @Schema(description = "Telangiectasias") private Boolean telangiectasias;
    @Schema(description = "Várices") private Boolean varices;
    @Schema(description = "Celulitis") private Boolean celulitis;
    @Schema(description = "Flacidez") private Boolean flacidez;
    @Schema(description = "Estrías") private Boolean estrias;
    @Schema(description = "Adiposidad localizada") private String adiposidadLocalizada;

    @Schema(description = "Peso actual en kilogramos", example = "65.50") private BigDecimal pesoActual;
    @Schema(description = "Peso habitual en kilogramos", example = "63.00") private BigDecimal pesoHabitual;
    @Schema(description = "Índice de masa corporal", example = "23.40") private BigDecimal imc;
    @Schema(description = "Perímetro de cintura en centímetros", example = "78.00") private BigDecimal perimetroCintura;

    @Schema(description = "Se toma fotografía") private Boolean seTomaFotografia;
    @Schema(description = "Diagnóstico y tratamiento") private String diagnosticoYTratamiento;
    @Schema(description = "Observaciones posteriores") private String observacionesPosteriores;

    @Schema(description = "Fecha y hora de creación") private LocalDateTime creadoEn;
    @Schema(description = "Fecha y hora de última actualización") private LocalDateTime actualizadoEn;
}
