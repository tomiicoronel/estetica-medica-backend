package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.HistoriaClinicaCorporalRequest;
import com.estetica.estetica.dto.response.HistoriaClinicaCorporalResponse;
import com.estetica.estetica.exception.ResourceAlreadyExistsException;
import com.estetica.estetica.model.HistoriaClinicaCorporal;
import com.estetica.estetica.model.Paciente;
import com.estetica.estetica.repository.HistoriaClinicaCorporalRepository;
import com.estetica.estetica.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class HistoriaClinicaCorporalService {

    private final HistoriaClinicaCorporalRepository historiaRepository;
    private final PacienteRepository pacienteRepository;

    @Transactional
    public HistoriaClinicaCorporalResponse crearFicha(UUID pacienteId, HistoriaClinicaCorporalRequest request) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el paciente con ID: " + pacienteId));

        if (historiaRepository.existsByPacienteId(pacienteId)) {
            throw new ResourceAlreadyExistsException(
                    "El paciente con ID " + pacienteId + " ya tiene una ficha clínica corporal");
        }

        HistoriaClinicaCorporal ficha = toEntity(new HistoriaClinicaCorporal(), request);
        ficha.setPaciente(paciente);

        HistoriaClinicaCorporal guardada = historiaRepository.save(ficha);
        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaCorporalResponse buscarPorPaciente(UUID pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new EntityNotFoundException(
                    "No se encontró el paciente con ID: " + pacienteId);
        }

        HistoriaClinicaCorporal ficha = historiaRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El paciente con ID " + pacienteId + " no tiene ficha clínica corporal cargada"));

        return toResponse(ficha);
    }

    @Transactional
    public HistoriaClinicaCorporalResponse actualizarFicha(UUID id, HistoriaClinicaCorporalRequest request) {
        HistoriaClinicaCorporal ficha = historiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la ficha clínica corporal con ID: " + id));

        toEntity(ficha, request);

        HistoriaClinicaCorporal actualizada = historiaRepository.saveAndFlush(ficha);
        return toResponse(actualizada);
    }

    // ============================================================
    // MÉTODOS PRIVADOS DE MAPEO
    // ============================================================

    private HistoriaClinicaCorporal toEntity(HistoriaClinicaCorporal ficha, HistoriaClinicaCorporalRequest r) {
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
        ficha.setCancer(r.getCancer());
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
        ficha.setLactancia(r.getLactancia());

        // Otros
        ficha.setHerpesLabial(r.getHerpesLabial());
        ficha.setMedicacionHabitual(r.getMedicacionHabitual());
        ficha.setAspirinaSemana(r.getAspirinaSemana());

        // Hábitos corporales
        ficha.setAlimentacionSaludable(r.getAlimentacionSaludable());
        ficha.setBebeAgua(r.getBebeAgua());
        ficha.setSedentarismoGimnasia(r.getSedentarismoGimnasia());
        ficha.setOrtostatismoProlongado(r.getOrtostatismoProlongado());
        ficha.setMediasCompresion(r.getMediasCompresion());
        ficha.setTratamientosPrevios(r.getTratamientosPrevios());
        ficha.setTratamientosPreviosCuales(r.getTratamientosPreviosCuales());
        ficha.setTratamientosPreviosCuando(r.getTratamientosPreviosCuando());
        ficha.setTratamientosPreviosRespuesta(r.getTratamientosPreviosRespuesta());
        ficha.setViajeProximoMes(r.getViajeProximoMes());

        // Examen corporal
        ficha.setPresenciaOtrosMateriales(r.getPresenciaOtrosMateriales());
        ficha.setSecuelasTratamientosPrevios(r.getSecuelasTratamientosPrevios());
        ficha.setAranasVasculares(r.getAranasVasculares());
        ficha.setTelangiectasias(r.getTelangiectasias());
        ficha.setVarices(r.getVarices());
        ficha.setCelulitis(r.getCelulitis());
        ficha.setFlacidez(r.getFlacidez());
        ficha.setEstrias(r.getEstrias());
        ficha.setAdiposidadLocalizada(r.getAdiposidadLocalizada());

        // Medidas antropométricas
        ficha.setPesoActual(r.getPesoActual());
        ficha.setPesoHabitual(r.getPesoHabitual());
        ficha.setImc(r.getImc());
        ficha.setPerimetroCintura(r.getPerimetroCintura());

        // Fotografía
        ficha.setSeTomaFotografia(r.getSeTomaFotografia());

        // Tratamiento y seguimiento
        ficha.setDiagnosticoYTratamiento(r.getDiagnosticoYTratamiento());
        ficha.setObservacionesPosteriores(r.getObservacionesPosteriores());

        return ficha;
    }

    private HistoriaClinicaCorporalResponse toResponse(HistoriaClinicaCorporal f) {
        return HistoriaClinicaCorporalResponse.builder()
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
                .cancer(f.getCancer())
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
                .lactancia(f.getLactancia())
                .herpesLabial(f.getHerpesLabial())
                .medicacionHabitual(f.getMedicacionHabitual())
                .aspirinaSemana(f.getAspirinaSemana())
                .alimentacionSaludable(f.getAlimentacionSaludable())
                .bebeAgua(f.getBebeAgua())
                .sedentarismoGimnasia(f.getSedentarismoGimnasia())
                .ortostatismoProlongado(f.getOrtostatismoProlongado())
                .mediasCompresion(f.getMediasCompresion())
                .tratamientosPrevios(f.getTratamientosPrevios())
                .tratamientosPreviosCuales(f.getTratamientosPreviosCuales())
                .tratamientosPreviosCuando(f.getTratamientosPreviosCuando())
                .tratamientosPreviosRespuesta(f.getTratamientosPreviosRespuesta())
                .viajeProximoMes(f.getViajeProximoMes())
                .presenciaOtrosMateriales(f.getPresenciaOtrosMateriales())
                .secuelasTratamientosPrevios(f.getSecuelasTratamientosPrevios())
                .aranasVasculares(f.getAranasVasculares())
                .telangiectasias(f.getTelangiectasias())
                .varices(f.getVarices())
                .celulitis(f.getCelulitis())
                .flacidez(f.getFlacidez())
                .estrias(f.getEstrias())
                .adiposidadLocalizada(f.getAdiposidadLocalizada())
                .pesoActual(f.getPesoActual())
                .pesoHabitual(f.getPesoHabitual())
                .imc(f.getImc())
                .perimetroCintura(f.getPerimetroCintura())
                .seTomaFotografia(f.getSeTomaFotografia())
                .diagnosticoYTratamiento(f.getDiagnosticoYTratamiento())
                .observacionesPosteriores(f.getObservacionesPosteriores())
                .creadoEn(f.getCreadoEn())
                .actualizadoEn(f.getActualizadoEn())
                .build();
    }
}

