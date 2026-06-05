package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "HistoriaClinicaFacialRequest", description = "Datos clínicos faciales. Todos los campos son opcionales para permitir completado progresivo.")
public class HistoriaClinicaFacialRequest {

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
    @Schema(description = "Otros antecedentes patológicos", example = "Rosácea") private String otroAntecedentePatologico;

    @Schema(description = "Tabaquismo") private Boolean tbq;
    @Schema(description = "Consumo de alcohol") private Boolean alcohol;
    @Schema(description = "Otros antecedentes tóxicos") private String otrasToxico;

    @Schema(description = "Alergia al huevo") private Boolean alergicoHuevo;
    @Schema(description = "Alergia a anestesia") private Boolean alergicoAnestesia;
    @Schema(description = "Alergia a pescado") private Boolean alergicoFish;
    @Schema(description = "Otras alergias") private String otrasAlergias;

    @Schema(description = "Antecedentes quirúrgicos") private String antecedentesQuirurgicos;
    @Schema(description = "Fecha de última menstruación o dato ginecológico textual")
    @Size(max = 255, message = "El campo FUM no puede superar los 255 caracteres")
    private String fum;
    @Schema(description = "Embarazo actual") private Boolean embarazo;

    @Schema(description = "Herpes labial") private Boolean herpesLabial;
    @Schema(description = "Medicación habitual") private String medicacionHabitual;
    @Schema(description = "Consumió aspirina en la última semana") private Boolean aspirinaSemana;

    @Schema(description = "Exposición solar frecuente") private Boolean exposicionSolar;
    @Schema(description = "Usa protección solar") private Boolean usaProteccionSolar;
    @Schema(description = "Tipo o marca de protección solar")
    @Size(max = 255, message = "El detalle de protección solar no puede superar los 255 caracteres")
    private String proteccionSolarCual;
    @Schema(description = "Veces por día que usa protección solar")
    @Size(max = 255, message = "La frecuencia de protección solar no puede superar los 255 caracteres")
    private String proteccionSolarVecesDia;
    @Schema(description = "Hábitos de higiene facial") private String habitosHigieneFacial;
    @Schema(description = "Tratamiento domiciliario actual") private String tratamientoDomiciliario;
    @Schema(description = "Tuvo tratamientos faciales previos") private Boolean tratamientosPrevios;
    @Schema(description = "Detalle de tratamientos previos") private String tratamientosPreviosCuales;
    @Schema(description = "Respuesta a tratamientos previos") private String tratamientosPreviosRespuesta;
    @Schema(description = "Viaje programado en el próximo mes") private Boolean viajeProximoMes;

    @Schema(description = "Presencia de otros materiales") private String presenciaOtrosMateriales;
    @Schema(description = "Secuelas de tratamientos previos") private String secuelasTratamientosPrevios;
    @Schema(description = "Se toma fotografía clínica") private Boolean seTomaFotografia;

    @Schema(description = "Fototipo de Fitzpatrick", example = "3", minimum = "1", maximum = "6")
    @Min(value = 1, message = "El fototipo de Fitzpatrick debe estar entre 1 y 6")
    @Max(value = 6, message = "El fototipo de Fitzpatrick debe estar entre 1 y 6")
    private Integer fototipoFitzpatrick;

    @Schema(description = "Grado de Glogau", example = "2", minimum = "1", maximum = "4")
    @Min(value = 1, message = "El grado de Glogau debe estar entre 1 y 4")
    @Max(value = 4, message = "El grado de Glogau debe estar entre 1 y 4")
    private Integer gradoGlogau;

    @Schema(description = "Diagnóstico y tratamiento indicado") private String diagnosticoYTratamiento;
    @Schema(description = "Observaciones posteriores") private String observacionesPosteriores;
}
