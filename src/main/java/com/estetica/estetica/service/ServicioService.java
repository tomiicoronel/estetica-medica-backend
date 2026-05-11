package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.ServicioRequest;
import com.estetica.estetica.dto.response.ServicioResponse;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.model.Servicio;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.estetica.estetica.repository.ServicioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final ProfesionalRepository profesionalRepository;

    @Transactional(readOnly = true)
    public List<ServicioResponse> listarPorProfesional(UUID profesionalId) {
        if (!profesionalRepository.existsById(profesionalId)) {
            throw new EntityNotFoundException(
                    "No se encontró la profesional con ID: " + profesionalId);
        }

        return servicioRepository.findByProfesionalId(profesionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ServicioResponse> listarActivosPorProfesional(UUID profesionalId) {
        if (!profesionalRepository.existsById(profesionalId)) {
            throw new EntityNotFoundException(
                    "No se encontró la profesional con ID: " + profesionalId);
        }

        return servicioRepository.findByProfesionalIdAndActivo(profesionalId, true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServicioResponse buscarPorId(UUID id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el servicio con ID: " + id));
        return toResponse(servicio);
    }

    @Transactional
    public ServicioResponse crear(ServicioRequest request) {
        Profesional profesional = profesionalRepository.findById(request.getProfesionalId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional con ID: " + request.getProfesionalId()));

        if (servicioRepository.existsByNombreAndProfesionalId(
                request.getNombre(), request.getProfesionalId())) {
            throw new IllegalArgumentException(
                    "La profesional ya tiene un servicio con el nombre: " + request.getNombre());
        }

        Servicio servicio = toEntity(request, profesional);
        Servicio guardado = servicioRepository.save(servicio);
        return toResponse(guardado);
    }

    @Transactional
    public ServicioResponse actualizar(UUID id, ServicioRequest request) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el servicio con ID: " + id));

        // Validar que no se intente reasignar el servicio a otra profesional
        if (!servicio.getProfesional().getId().equals(request.getProfesionalId())) {
            throw new IllegalArgumentException(
                    "No se puede reasignar un servicio a otra profesional. "
                    + "El servicio pertenece a la profesional con ID: "
                    + servicio.getProfesional().getId());
        }

        // Si cambió el nombre, verificar que no esté duplicado para esa profesional
        if (!servicio.getNombre().equals(request.getNombre())
                && servicioRepository.existsByNombreAndProfesionalId(
                request.getNombre(), servicio.getProfesional().getId())) {
            throw new IllegalArgumentException(
                    "La profesional ya tiene un servicio con el nombre: " + request.getNombre());
        }

        servicio.setNombre(request.getNombre());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setPrecio(request.getPrecio());

        Servicio actualizado = servicioRepository.saveAndFlush(servicio);
        return toResponse(actualizado);
    }

    @Transactional
    public void cambiarEstado(UUID id, Boolean activo) {
        if (!servicioRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "No se encontró el servicio con ID: " + id);
        }

        int filasAfectadas = servicioRepository.cambiarEstado(id, activo);
        if (filasAfectadas == 0) {
            throw new EntityNotFoundException(
                    "No se pudo actualizar el estado del servicio con ID: " + id);
        }
    }

    @Transactional
    public void actualizarPrecio(UUID id, java.math.BigDecimal nuevoPrecio) {
        if (!servicioRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "No se encontró el servicio con ID: " + id);
        }

        if (nuevoPrecio == null || nuevoPrecio.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        int filasAfectadas = servicioRepository.actualizarPrecio(id, nuevoPrecio);
        if (filasAfectadas == 0) {
            throw new EntityNotFoundException(
                    "No se pudo actualizar el precio del servicio con ID: " + id);
        }
    }

    private ServicioResponse toResponse(Servicio servicio) {
        return ServicioResponse.builder()
                .id(servicio.getId())
                .profesionalId(servicio.getProfesional().getId())
                .nombre(servicio.getNombre())
                .descripcion(servicio.getDescripcion())
                .precio(servicio.getPrecio())
                .activo(servicio.getActivo())
                .creadoEn(servicio.getCreadoEn())
                .actualizadoEn(servicio.getActualizadoEn())
                .build();
    }

    private Servicio toEntity(ServicioRequest request, Profesional profesional) {
        return Servicio.builder()
                .profesional(profesional)
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .build();
    }
}
