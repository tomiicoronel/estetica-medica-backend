package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.ProfesionalRequest;
import com.estetica.estetica.dto.response.ProfesionalResponse;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.repository.ProfesionalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfesionalService {

    private final ProfesionalRepository profesionalRepository;

    @Transactional(readOnly = true)
    public List<ProfesionalResponse> listarTodos() {
        return profesionalRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProfesionalResponse buscarPorId(UUID id) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional con ID: " + id));
        return toResponse(profesional);
    }

    @Transactional
    public ProfesionalResponse crear(ProfesionalRequest request) {
        if (profesionalRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe una profesional con el email: " + request.getEmail());
        }

        Profesional profesional = toEntity(request);
        Profesional guardado = profesionalRepository.save(profesional);
        return toResponse(guardado);
    }

    @Transactional
    public ProfesionalResponse actualizar(UUID id, ProfesionalRequest request) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional con ID: " + id));

        // Si cambió el email, verificar que no esté en uso por otra profesional
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

    @Transactional
    public void eliminar(UUID id) {
        if (!profesionalRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "No se encontró la profesional con ID: " + id);
        }
        profesionalRepository.deleteById(id);
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

    private Profesional toEntity(ProfesionalRequest request) {
        return Profesional.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .especialidad(request.getEspecialidad())
                .build();
    }
}
