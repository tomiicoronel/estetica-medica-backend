package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.SesionClinicaRequest;
import com.estetica.estetica.dto.response.SesionClinicaResponse;
import com.estetica.estetica.exception.AccesoNoAutorizadoException;
import com.estetica.estetica.exception.ResourceAlreadyExistsException;
import com.estetica.estetica.model.EstadoTurno;
import com.estetica.estetica.model.Paciente;
import com.estetica.estetica.model.SesionClinica;
import com.estetica.estetica.model.Turno;
import com.estetica.estetica.repository.PacienteRepository;
import com.estetica.estetica.repository.SesionClinicaRepository;
import com.estetica.estetica.repository.TurnoRepository;
import com.estetica.estetica.security.ProfesionalAutenticadaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SesionClinicaService {

    private final SesionClinicaRepository sesionClinicaRepository;
    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfesionalAutenticadaService profesionalAutenticadaService;

    @Transactional
    public SesionClinicaResponse crear(UUID turnoId, SesionClinicaRequest request) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el turno con ID: " + turnoId));
        validarTurnoPerteneceAProfesionalAutenticada(turno);

        if (!EstadoTurno.REALIZADO.equals(turno.getEstado())) {
            throw new IllegalArgumentException("Solo se puede registrar una sesión clínica para un turno en estado REALIZADO");
        }

        if (sesionClinicaRepository.existsByTurnoId(turnoId)) {
            throw new ResourceAlreadyExistsException("El turno con ID " + turnoId + " ya tiene una sesión clínica registrada");
        }

        UUID pacienteId = turno.getPaciente().getId();
        int numeroSesion = Math.toIntExact(sesionClinicaRepository.countByTurno_Paciente_Id(pacienteId) + 1);

        SesionClinica sesion = SesionClinica.builder()
                .turno(turno)
                .numeroSesion(numeroSesion)
                .tratamiento(request.getTratamiento())
                .respuestaTolerancia(request.getRespuestaTolerancia())
                .observaciones(request.getObservaciones())
                .build();

        SesionClinica guardada = sesionClinicaRepository.save(sesion);
        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public SesionClinicaResponse buscarPorId(UUID id) {
        SesionClinica sesion = buscarEntidadPropia(id);
        return toResponse(sesion);
    }

    @Transactional(readOnly = true)
    public SesionClinicaResponse buscarPorTurno(UUID turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el turno con ID: " + turnoId));
        validarTurnoPerteneceAProfesionalAutenticada(turno);

        SesionClinica sesion = sesionClinicaRepository.findByTurnoId(turnoId)
                .orElseThrow(() -> new EntityNotFoundException("El turno con ID " + turnoId + " no tiene sesión clínica registrada"));
        return toResponse(sesion);
    }

    @Transactional(readOnly = true)
    public List<SesionClinicaResponse> listarPorPaciente(UUID pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el paciente con ID: " + pacienteId));
        validarPacientePerteneceAProfesionalAutenticada(paciente);

        return sesionClinicaRepository.findByTurno_Paciente_IdOrderByNumeroSesionAsc(pacienteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SesionClinicaResponse actualizar(UUID id, SesionClinicaRequest request) {
        SesionClinica sesion = buscarEntidadPropia(id);

        sesion.setTratamiento(request.getTratamiento());
        sesion.setRespuestaTolerancia(request.getRespuestaTolerancia());
        sesion.setObservaciones(request.getObservaciones());

        SesionClinica actualizada = sesionClinicaRepository.saveAndFlush(sesion);
        return toResponse(actualizada);
    }

    private SesionClinica buscarEntidadPropia(UUID id) {
        SesionClinica sesion = sesionClinicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la sesión clínica con ID: " + id));
        validarTurnoPerteneceAProfesionalAutenticada(sesion.getTurno());
        return sesion;
    }

    private void validarTurnoPerteneceAProfesionalAutenticada(Turno turno) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        if (!turno.getProfesional().getId().equals(profesionalId)) {
            throw new AccesoNoAutorizadoException("No se encontró el turno con ID: " + turno.getId());
        }
    }

    private void validarPacientePerteneceAProfesionalAutenticada(Paciente paciente) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        if (!paciente.getProfesional().getId().equals(profesionalId)) {
            throw new AccesoNoAutorizadoException("No se encontró el paciente con ID: " + paciente.getId());
        }
    }

    private SesionClinicaResponse toResponse(SesionClinica sesion) {
        Turno turno = sesion.getTurno();
        Paciente paciente = turno.getPaciente();

        return SesionClinicaResponse.builder()
                .id(sesion.getId())
                .turnoId(turno.getId())
                .pacienteId(paciente.getId())
                .profesionalId(turno.getProfesional().getId())
                .numeroSesion(sesion.getNumeroSesion())
                .tratamiento(sesion.getTratamiento())
                .respuestaTolerancia(sesion.getRespuestaTolerancia())
                .observaciones(sesion.getObservaciones())
                .creadoEn(sesion.getCreadoEn())
                .actualizadoEn(sesion.getActualizadoEn())
                .build();
    }
}

