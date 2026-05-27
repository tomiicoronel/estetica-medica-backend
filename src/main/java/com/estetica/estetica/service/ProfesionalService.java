package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.ProfesionalRequest;
import com.estetica.estetica.dto.response.ProfesionalResponse;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.estetica.estetica.security.ProfesionalAutenticadaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfesionalService {

    private final ProfesionalRepository profesionalRepository;
    private final ProfesionalAutenticadaService profesionalAutenticadaService;

    @Transactional(readOnly = true)
    public ProfesionalResponse obtenerPerfilAutenticado() {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional autenticada con ID: " + profesionalId));
        return toResponse(profesional);
    }

    @Transactional
    public ProfesionalResponse actualizarPerfilAutenticado(ProfesionalRequest request) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional autenticada con ID: " + profesionalId));

        if (!profesional.getEmail().equals(request.getEmail())
                && profesionalRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe una profesional con el email: " + request.getEmail());
        }

        profesional.setNombre(request.getNombre());
        profesional.setApellido(request.getApellido());
        profesional.setEmail(request.getEmail());
        profesional.setTelefono(request.getTelefono());
        profesional.setEspecialidad(request.getEspecialidad());

        Profesional actualizado = profesionalRepository.saveAndFlush(profesional);
        return toResponse(actualizado);
    }

    private ProfesionalResponse toResponse(Profesional profesional) {
        return ProfesionalResponse.builder()
                .id(profesional.getId())
                .nombre(profesional.getNombre())
                .apellido(profesional.getApellido())
                .email(profesional.getEmail())
                .telefono(profesional.getTelefono())
                .especialidad(profesional.getEspecialidad())
                .creadoEn(profesional.getCreadoEn())
                .actualizadoEn(profesional.getActualizadoEn())
                .build();
    }
}
