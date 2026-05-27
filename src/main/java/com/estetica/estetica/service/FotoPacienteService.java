package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.FotoPacienteRequest;
import com.estetica.estetica.dto.response.FotoPacienteResponse;
import com.estetica.estetica.exception.AccesoNoAutorizadoException;
import com.estetica.estetica.model.FotoPaciente;
import com.estetica.estetica.model.Paciente;
import com.estetica.estetica.model.SesionClinica;
import com.estetica.estetica.repository.FotoPacienteRepository;
import com.estetica.estetica.repository.PacienteRepository;
import com.estetica.estetica.repository.SesionClinicaRepository;
import com.estetica.estetica.security.ProfesionalAutenticadaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FotoPacienteService {

    private final FotoPacienteRepository fotoPacienteRepository;
    private final SesionClinicaRepository sesionClinicaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfesionalAutenticadaService profesionalAutenticadaService;

    @Transactional
    public FotoPacienteResponse registrar(UUID sesionId, FotoPacienteRequest request) {
        if (!sesionId.equals(request.getSesionClinicaId())) {
            throw new IllegalArgumentException("El ID de sesión clínica del path no coincide con el ID enviado en el body");
        }

        SesionClinica sesion = sesionClinicaRepository.findById(sesionId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la sesión clínica con ID: " + sesionId));
        validarSesionPerteneceAProfesionalAutenticada(sesion);

        Paciente paciente = sesion.getTurno().getPaciente();
        UUID fotoArchivoId = UUID.randomUUID();

        FotoPaciente foto = FotoPaciente.builder()
                .paciente(paciente)
                .sesionClinica(sesion)
                .rutaImagen(generarRutaImagen(paciente.getId(), sesion.getId(), fotoArchivoId))
                .fecha(LocalDateTime.now())
                .descripcion(request.getDescripcion())
                .build();

        FotoPaciente guardada = fotoPacienteRepository.save(foto);
        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<FotoPacienteResponse> listarPorPaciente(UUID pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el paciente con ID: " + pacienteId));
        validarPacientePerteneceAProfesionalAutenticada(paciente);

        return fotoPacienteRepository.findByPacienteId(pacienteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FotoPacienteResponse> listarPorSesion(UUID sesionId) {
        SesionClinica sesion = sesionClinicaRepository.findById(sesionId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la sesión clínica con ID: " + sesionId));
        validarSesionPerteneceAProfesionalAutenticada(sesion);

        return fotoPacienteRepository.findBySesionClinicaId(sesionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void eliminar(UUID id) {
        FotoPaciente foto = fotoPacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la foto con ID: " + id));
        validarPacientePerteneceAProfesionalAutenticada(foto.getPaciente());
        fotoPacienteRepository.delete(foto);

        // TODO: cuando se integre almacenamiento real, eliminar también el archivo físico/S3 asociado a la foto.
    }

    private void validarSesionPerteneceAProfesionalAutenticada(SesionClinica sesion) {
        validarPacientePerteneceAProfesionalAutenticada(sesion.getTurno().getPaciente());
    }

    private void validarPacientePerteneceAProfesionalAutenticada(Paciente paciente) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        if (!paciente.getProfesional().getId().equals(profesionalId)) {
            throw new AccesoNoAutorizadoException("No se encontró el paciente con ID: " + paciente.getId());
        }
    }

    private String generarRutaImagen(UUID pacienteId, UUID sesionId, UUID fotoArchivoId) {
        return "uploads/pacientes/%s/sesiones/%s/foto-%s.jpg".formatted(pacienteId, sesionId, fotoArchivoId);
    }

    private FotoPacienteResponse toResponse(FotoPaciente foto) {
        return FotoPacienteResponse.builder()
                .id(foto.getId())
                .pacienteId(foto.getPaciente().getId())
                .sesionClinicaId(foto.getSesionClinica().getId())
                .rutaImagen(foto.getRutaImagen())
                .fecha(foto.getFecha())
                .descripcion(foto.getDescripcion())
                .creadoEn(foto.getCreadoEn())
                .build();
    }
}

