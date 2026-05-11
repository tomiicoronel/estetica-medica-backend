package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "HistoriaClinicaCorporalRequest", description = "Datos clínicos corporales. Todos los campos son opcionales para permitir completado progresivo.")
public class HistoriaClinicaCorporalRequest {

    @Schema(description = "Antecedente patológico: hipertensión arterial") private Boolean hta;
    @Schema(description = "Antecedente patológico: diabetes") private Boolean dbt;
    @Schema(description = "Antecedente patológico: hipotiroidismo") private Boolean hipotiroidismo;
    @Schema(description = "Antecedente patológico: hipertiroidismo") private Boolean hipertiroidismo;
    @Schema(description = "Antecedente patológico: anemia") private Boolean anemia;
    @Schema(description = "Antecedente patológico: enfermedades autoinmunes") private Boolean enfermedadesAutoinmunes;
    @Schema(description = "Antecedente patológico: glaucoma") private Boolean glaucoma;
    @Schema(description = "Antecedente patológico: enfermedad neuromuscular") private Boolean enfermedadNeuromuscular;
    @Schema(description = "Antecedente patológico: trastornos de coagulación") private Boolean trastornosCoagulacion;
    @Schema(description = "Antecedente patológico: alteración de cicatrización") private Boolean alteracionCicatrizacion;
    @Schema(description = "Antecedente patológico: marcapasos") private Boolean marcapasos;
    @Schema(description = "Antecedente patológico: prótesis metálica") private Boolean protesisMetalica;
    @Schema(description = "Antecedente patológico: cáncer") private Boolean cancer;
    @Schema(description = "Otros antecedentes patológicos", example = "Hipertensión controlada con medicación") private String otroAntecedentePatologico;

    @Schema(description = "Tabaquismo") private Boolean tbq;
    @Schema(description = "Consumo de alcohol") private Boolean alcohol;
    @Schema(description = "Otros antecedentes tóxicos") private String otrasToxico;

    @Schema(description = "Alergia al huevo") private Boolean alergicoHuevo;
    @Schema(description = "Alergia a anestesia") private Boolean alergicoAnestesia;
    @Schema(description = "Alergia a pescado") private Boolean alergicoFish;
    @Schema(description = "Otras alergias") private String otrasAlergias;

    @Schema(description = "Antecedentes quirúrgicos") private String antecedentesQuirurgicos;
    @Schema(description = "Fecha de última menstruación o dato ginecológico textual") private String fum;
    @Schema(description = "Embarazo actual") private Boolean embarazo;
    @Schema(description = "Lactancia actual") private Boolean lactancia;

    @Schema(description = "Herpes labial") private Boolean herpesLabial;
    @Schema(description = "Medicación habitual") private String medicacionHabitual;
    @Schema(description = "Consumió aspirina en la última semana") private Boolean aspirinaSemana;

    @Schema(description = "Alimentación saludable") private Boolean alimentacionSaludable;
    @Schema(description = "Bebe agua habitualmente") private Boolean bebeAgua;
    @Schema(description = "Sedentarismo o actividad física", example = "Camina 2 veces por semana") private String sedentarismoGimnasia;
    @Schema(description = "Permanece muchas horas de pie") private Boolean ortostatismoProlongado;
    @Schema(description = "Usa medias de compresión") private Boolean mediasCompresion;
    @Schema(description = "Tuvo tratamientos corporales previos") private Boolean tratamientosPrevios;
    @Schema(description = "Tratamientos corporales previos realizados") private String tratamientosPreviosCuales;
    @Schema(description = "Cuándo se realizaron los tratamientos previos") private String tratamientosPreviosCuando;
    @Schema(description = "Respuesta a tratamientos previos") private String tratamientosPreviosRespuesta;
    @Schema(description = "Viaje programado en el próximo mes") private Boolean viajeProximoMes;

    @Schema(description = "Presencia de otros materiales") private String presenciaOtrosMateriales;
    @Schema(description = "Secuelas de tratamientos previos") private String secuelasTratamientosPrevios;
    @Schema(description = "Arañas vasculares") private Boolean aranasVasculares;
    @Schema(description = "Telangiectasias") private Boolean telangiectasias;
    @Schema(description = "Várices") private Boolean varices;
    @Schema(description = "Celulitis") private Boolean celulitis;
    @Schema(description = "Flacidez") private Boolean flacidez;
    @Schema(description = "Estrías") private Boolean estrias;
    @Schema(description = "Adiposidad localizada", example = "Abdomen y flancos") private String adiposidadLocalizada;

    @Schema(description = "Peso actual en kilogramos", example = "65.50", minimum = "0.0")
    @DecimalMin(value = "0.0", message = "El peso actual no puede ser negativo")
    private BigDecimal pesoActual;

    @Schema(description = "Peso habitual en kilogramos", example = "63.00", minimum = "0.0")
    @DecimalMin(value = "0.0", message = "El peso habitual no puede ser negativo")
    private BigDecimal pesoHabitual;

    @Schema(description = "Índice de masa corporal", example = "23.40", minimum = "0.0")
    @DecimalMin(value = "0.0", message = "El IMC no puede ser negativo")
    private BigDecimal imc;

    @Schema(description = "Perímetro de cintura en centímetros", example = "78.00", minimum = "0.0")
    @DecimalMin(value = "0.0", message = "El perímetro de cintura no puede ser negativo")
    private BigDecimal perimetroCintura;

    @Schema(description = "Se toma fotografía clínica") private Boolean seTomaFotografia;
    @Schema(description = "Diagnóstico y tratamiento indicado") private String diagnosticoYTratamiento;
    @Schema(description = "Observaciones posteriores") private String observacionesPosteriores;
}
