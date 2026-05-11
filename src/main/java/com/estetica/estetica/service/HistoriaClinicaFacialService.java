package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.HistoriaClinicaFacialRequest;
import com.estetica.estetica.dto.response.HistoriaClinicaFacialResponse;
import com.estetica.estetica.exception.ResourceAlreadyExistsException;
import com.estetica.estetica.model.HistoriaClinicaFacial;
import com.estetica.estetica.model.Paciente;
import com.estetica.estetica.repository.HistoriaClinicaFacialRepository;
import com.estetica.estetica.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class HistoriaClinicaFacialService {

    private final HistoriaClinicaFacialRepository historiaRepository;
    private final PacienteRepository pacienteRepository;

    @Transactional
    public HistoriaClinicaFacialResponse crearFicha(UUID pacienteId, HistoriaClinicaFacialRequest request) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el paciente con ID: " + pacienteId));

        if (historiaRepository.existsByPacienteId(pacienteId)) {
            throw new ResourceAlreadyExistsException(
                    "El paciente con ID " + pacienteId + " ya tiene una ficha clínica facial");
        }

        HistoriaClinicaFacial ficha = toEntity(new HistoriaClinicaFacial(), request);
        ficha.setPaciente(paciente);

        HistoriaClinicaFacial guardada = historiaRepository.save(ficha);
        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaFacialResponse buscarPorPaciente(UUID pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new EntityNotFoundException(
                    "No se encontró el paciente con ID: " + pacienteId);
        }

        HistoriaClinicaFacial ficha = historiaRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El paciente con ID " + pacienteId + " no tiene ficha clínica facial cargada"));

        return toResponse(ficha);
    }

    @Transactional
    public HistoriaClinicaFacialResponse actualizarFicha(UUID id, HistoriaClinicaFacialRequest request) {
        HistoriaClinicaFacial ficha = historiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la ficha clínica facial con ID: " + id));

        toEntity(ficha, request);

        HistoriaClinicaFacial actualizada = historiaRepository.saveAndFlush(ficha);
        return toResponse(actualizada);
    }

    // ============================================================
    // MÉTODOS PRIVADOS DE MAPEO
    // ============================================================

    private HistoriaClinicaFacial toEntity(HistoriaClinicaFacial ficha, HistoriaClinicaFacialRequest r) {
        // Antecedentes patológicos
        ficha.setHta(r.getHta());
        ficha.setDbt(r.getDbt());
        ficha.setHipotiroidismo(r.getHipotiroidismo());
        ficha.setHipertiroidismo(r.getHipertiroidismo());
        ficha.setAnemia(r.getAnemia());
        ficha.setEnfermedadesAutoinmunes(r.getEnfermedadesAutoinmunes());
        ficha.setGlaucoma(r.getGlaucoma());
        ficha.setEnfermedadNeuromuscular(r.getEnfermedadNeuromuscular());
        ficha.setTrastornosCoagulacion(r.getTrastornosCoagulacion());
        ficha.setAlteracionCicatrizacion(r.getAlteracionCicatrizacion());
        ficha.setMarcapasos(r.getMarcapasos());
        ficha.setProtesisMetalica(r.getProtesisMetalica());
        ficha.setOtroAntecedentePatologico(r.getOtroAntecedentePatologico());

        // Tóxicos
        ficha.setTbq(r.getTbq());
        ficha.setAlcohol(r.getAlcohol());
        ficha.setOtrasToxico(r.getOtrasToxico());

        // Alérgicos
        ficha.setAlergicoHuevo(r.getAlergicoHuevo());
        ficha.setAlergicoAnestesia(r.getAlergicoAnestesia());
        ficha.setAlergicoFish(r.getAlergicoFish());
        ficha.setOtrasAlergias(r.getOtrasAlergias());

        // Quirúrgicos
        ficha.setAntecedentesQuirurgicos(r.getAntecedentesQuirurgicos());

        // Ginecológicos
        ficha.setFum(r.getFum());
        ficha.setEmbarazo(r.getEmbarazo());

        // Otros
        ficha.setHerpesLabial(r.getHerpesLabial());
        ficha.setMedicacionHabitual(r.getMedicacionHabitual());
        ficha.setAspirinaSemana(r.getAspirinaSemana());

        // Hábitos
        ficha.setExposicionSolar(r.getExposicionSolar());
        ficha.setUsaProteccionSolar(r.getUsaProteccionSolar());
        ficha.setProteccionSolarCual(r.getProteccionSolarCual());
        ficha.setProteccionSolarVecesDia(r.getProteccionSolarVecesDia());
        ficha.setHabitosHigieneFacial(r.getHabitosHigieneFacial());
        ficha.setTratamientoDomiciliario(r.getTratamientoDomiciliario());
        ficha.setTratamientosPrevios(r.getTratamientosPrevios());
        ficha.setTratamientosPreviosCuales(r.getTratamientosPreviosCuales());
        ficha.setTratamientosPreviosRespuesta(r.getTratamientosPreviosRespuesta());
        ficha.setViajeProximoMes(r.getViajeProximoMes());

        // Examen
        ficha.setPresenciaOtrosMateriales(r.getPresenciaOtrosMateriales());
        ficha.setSecuelasTratamientosPrevios(r.getSecuelasTratamientosPrevios());
        ficha.setSeTomaFotografia(r.getSeTomaFotografia());

        // Clasificaciones
        ficha.setFototipoFitzpatrick(r.getFototipoFitzpatrick());
        ficha.setGradoGlogau(r.getGradoGlogau());

        // Tratamiento y seguimiento
        ficha.setDiagnosticoYTratamiento(r.getDiagnosticoYTratamiento());
        ficha.setObservacionesPosteriores(r.getObservacionesPosteriores());

        return ficha;
    }

    private HistoriaClinicaFacialResponse toResponse(HistoriaClinicaFacial f) {
        return HistoriaClinicaFacialResponse.builder()
                .id(f.getId())
                .pacienteId(f.getPaciente().getId())
                .hta(f.getHta())
                .dbt(f.getDbt())
                .hipotiroidismo(f.getHipotiroidismo())
                .hipertiroidismo(f.getHipertiroidismo())
                .anemia(f.getAnemia())
                .enfermedadesAutoinmunes(f.getEnfermedadesAutoinmunes())
                .glaucoma(f.getGlaucoma())
                .enfermedadNeuromuscular(f.getEnfermedadNeuromuscular())
                .trastornosCoagulacion(f.getTrastornosCoagulacion())
                .alteracionCicatrizacion(f.getAlteracionCicatrizacion())
                .marcapasos(f.getMarcapasos())
                .protesisMetalica(f.getProtesisMetalica())
                .otroAntecedentePatologico(f.getOtroAntecedentePatologico())
                .tbq(f.getTbq())
                .alcohol(f.getAlcohol())
                .otrasToxico(f.getOtrasToxico())
                .alergicoHuevo(f.getAlergicoHuevo())
                .alergicoAnestesia(f.getAlergicoAnestesia())
                .alergicoFish(f.getAlergicoFish())
                .otrasAlergias(f.getOtrasAlergias())
                .antecedentesQuirurgicos(f.getAntecedentesQuirurgicos())
                .fum(f.getFum())
                .embarazo(f.getEmbarazo())
                .herpesLabial(f.getHerpesLabial())
                .medicacionHabitual(f.getMedicacionHabitual())
                .aspirinaSemana(f.getAspirinaSemana())
                .exposicionSolar(f.getExposicionSolar())
                .usaProteccionSolar(f.getUsaProteccionSolar())
                .proteccionSolarCual(f.getProteccionSolarCual())
                .proteccionSolarVecesDia(f.getProteccionSolarVecesDia())
                .habitosHigieneFacial(f.getHabitosHigieneFacial())
                .tratamientoDomiciliario(f.getTratamientoDomiciliario())
                .tratamientosPrevios(f.getTratamientosPrevios())
                .tratamientosPreviosCuales(f.getTratamientosPreviosCuales())
                .tratamientosPreviosRespuesta(f.getTratamientosPreviosRespuesta())
                .viajeProximoMes(f.getViajeProximoMes())
                .presenciaOtrosMateriales(f.getPresenciaOtrosMateriales())
                .secuelasTratamientosPrevios(f.getSecuelasTratamientosPrevios())
                .seTomaFotografia(f.getSeTomaFotografia())
                .fototipoFitzpatrick(f.getFototipoFitzpatrick())
                .gradoGlogau(f.getGradoGlogau())
                .diagnosticoYTratamiento(f.getDiagnosticoYTratamiento())
                .observacionesPosteriores(f.getObservacionesPosteriores())
                .creadoEn(f.getCreadoEn())
                .actualizadoEn(f.getActualizadoEn())
                .build();
    }
}

