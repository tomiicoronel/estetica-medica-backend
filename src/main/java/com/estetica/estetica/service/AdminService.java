package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.CrearProfesionalRequest;
import com.estetica.estetica.dto.request.EditarProfesionalRequest;
import com.estetica.estetica.dto.request.ResetearPasswordRequest;
import com.estetica.estetica.dto.response.ProfesionalResponse;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.model.RolUsuario;
import com.estetica.estetica.repository.ProfesionalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProfesionalRepository profesionalRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ProfesionalResponse crearProfesional(CrearProfesionalRequest request) {
        if (profesionalRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe una profesional con el email: " + request.getEmail());
        }

        Profesional profesional = Profesional.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .especialidad(request.getEspecialidad())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(RolUsuario.PROFESIONAL)
                .debeCambiarPassword(true)
                .build();

        Profesional guardada = profesionalRepository.save(profesional);
        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<ProfesionalResponse> listarProfesionales() {
        return profesionalRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProfesionalResponse editarProfesional(UUID id, EditarProfesionalRequest request) {
        Profesional profesional = buscarProfesional(id);

        if (!profesional.getEmail().equals(request.getEmail())
                && profesionalRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("Ya existe una profesional con el email: " + request.getEmail());
        }

        profesional.setNombre(request.getNombre());
        profesional.setApellido(request.getApellido());
        profesional.setEmail(request.getEmail());
        profesional.setTelefono(request.getTelefono());
        profesional.setEspecialidad(request.getEspecialidad());

        Profesional guardada = profesionalRepository.save(profesional);
        return toResponse(guardada);
    }

    @Transactional
    public void resetearPassword(UUID id, ResetearPasswordRequest request) {
        Profesional profesional = buscarProfesional(id);
        profesional.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
        profesional.setDebeCambiarPassword(true);
        profesionalRepository.save(profesional);
    }

    @Transactional
    public void darDeBaja(UUID id) {
        Profesional profesional = buscarProfesional(id);
        profesionalRepository.delete(profesional);
    }

    private Profesional buscarProfesional(UUID id) {
        return profesionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la profesional con ID: " + id));
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
