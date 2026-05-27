package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.PacienteRequest;
import com.estetica.estetica.dto.response.PacienteResponse;
import com.estetica.estetica.exception.AccesoNoAutorizadoException;
import com.estetica.estetica.model.Paciente;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.repository.PacienteRepository;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.estetica.estetica.security.ProfesionalAutenticadaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ProfesionalRepository profesionalRepository;
    private final ProfesionalAutenticadaService profesionalAutenticadaService;

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarPorProfesional() {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();

        return pacienteRepository.findByProfesionalId(profesionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarActivosPorProfesional() {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();

        return pacienteRepository.findByProfesionalIdAndActivo(profesionalId, true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(UUID id) {
        Paciente paciente = buscarEntidadPropia(id);
        return toResponse(paciente);
    }

    @Transactional
    public PacienteResponse crear(PacienteRequest request) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional autenticada con ID: " + profesionalId));

        if (pacienteRepository.existsByDniCuitAndProfesionalId(
                request.getDniCuit(), profesionalId)) {
            throw new IllegalArgumentException(
                    "Ya existe un paciente con DNI/CUIT " + request.getDniCuit()
                    + " para esta profesional");
        }

        Paciente paciente = toEntity(request, profesional);
        Paciente guardado = pacienteRepository.save(paciente);
        return toResponse(guardado);
    }

    @Transactional
    public PacienteResponse actualizar(UUID id, PacienteRequest request) {
        Paciente paciente = buscarEntidadPropia(id);
        UUID profesionalId = paciente.getProfesional().getId();

        // Si cambió el DNI/CUIT, verificar que no esté duplicado
        if (!paciente.getDniCuit().equals(request.getDniCuit())
                && pacienteRepository.existsByDniCuitAndProfesionalId(
                request.getDniCuit(), profesionalId)) {
            throw new IllegalArgumentException(
                    "Ya existe un paciente con DNI/CUIT " + request.getDniCuit()
                    + " para esta profesional");
        }

        // Actualizar todos los campos
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setDniCuit(request.getDniCuit());
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setTelefono(request.getTelefono());
        paciente.setEmail(request.getEmail());
        paciente.setProfesion(request.getProfesion());
        paciente.setDomicilio(request.getDomicilio());
        paciente.setObraSocial(request.getObraSocial());
        paciente.setNumeroObraSocial(request.getNumeroObraSocial());
        paciente.setContactoEmergenciaNombre(request.getContactoEmergenciaNombre());
        paciente.setContactoEmergenciaTelefono(request.getContactoEmergenciaTelefono());
        paciente.setContactoEmergenciaParentesco(request.getContactoEmergenciaParentesco());
        paciente.setEntidadTraslado1(request.getEntidadTraslado1());
        paciente.setEntidadTraslado2(request.getEntidadTraslado2());

        Paciente actualizado = pacienteRepository.saveAndFlush(paciente);
        return toResponse(actualizado);
    }

    @Transactional
    public void cambiarEstado(UUID id, Boolean activo) {
        buscarEntidadPropia(id);

        int filasAfectadas = pacienteRepository.cambiarEstado(id, activo);
        if (filasAfectadas == 0) {
            throw new EntityNotFoundException(
                    "No se pudo actualizar el estado del paciente con ID: " + id);
        }
    }

    Paciente buscarEntidadPropia(UUID id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el paciente con ID: " + id));
        validarPerteneceAProfesionalAutenticada(paciente);
        return paciente;
    }

    void validarPerteneceAProfesionalAutenticada(Paciente paciente) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        if (!paciente.getProfesional().getId().equals(profesionalId)) {
            throw new AccesoNoAutorizadoException("No se encontró el paciente con ID: " + paciente.getId());
        }
    }

    private PacienteResponse toResponse(Paciente paciente) {
        return PacienteResponse.builder()
                .id(paciente.getId())
                .profesionalId(paciente.getProfesional().getId())
                .nombre(paciente.getNombre())
                .apellido(paciente.getApellido())
                .dniCuit(paciente.getDniCuit())
                .fechaNacimiento(paciente.getFechaNacimiento())
                .telefono(paciente.getTelefono())
                .email(paciente.getEmail())
                .profesion(paciente.getProfesion())
                .domicilio(paciente.getDomicilio())
                .obraSocial(paciente.getObraSocial())
                .numeroObraSocial(paciente.getNumeroObraSocial())
                .contactoEmergenciaNombre(paciente.getContactoEmergenciaNombre())
                .contactoEmergenciaTelefono(paciente.getContactoEmergenciaTelefono())
                .contactoEmergenciaParentesco(paciente.getContactoEmergenciaParentesco())
                .entidadTraslado1(paciente.getEntidadTraslado1())
                .entidadTraslado2(paciente.getEntidadTraslado2())
                .activo(paciente.getActivo())
                .creadoEn(paciente.getCreadoEn())
                .actualizadoEn(paciente.getActualizadoEn())
                .build();
    }

    private Paciente toEntity(PacienteRequest request, Profesional profesional) {
        return Paciente.builder()
                .profesional(profesional)
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .dniCuit(request.getDniCuit())
                .fechaNacimiento(request.getFechaNacimiento())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .profesion(request.getProfesion())
                .domicilio(request.getDomicilio())
                .obraSocial(request.getObraSocial())
                .numeroObraSocial(request.getNumeroObraSocial())
                .contactoEmergenciaNombre(request.getContactoEmergenciaNombre())
                .contactoEmergenciaTelefono(request.getContactoEmergenciaTelefono())
                .contactoEmergenciaParentesco(request.getContactoEmergenciaParentesco())
                .entidadTraslado1(request.getEntidadTraslado1())
                .entidadTraslado2(request.getEntidadTraslado2())
                .build();
    }
}

