package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.ServicioRequest;
import com.estetica.estetica.dto.response.ServicioResponse;
import com.estetica.estetica.exception.AccesoNoAutorizadoException;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.model.Servicio;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.estetica.estetica.repository.ServicioRepository;
import com.estetica.estetica.security.ProfesionalAutenticadaService;
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
    private final ProfesionalAutenticadaService profesionalAutenticadaService;

    @Transactional(readOnly = true)
    public List<ServicioResponse> listarPorProfesional() {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();

        return servicioRepository.findByProfesionalId(profesionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ServicioResponse> listarActivosPorProfesional() {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();

        return servicioRepository.findByProfesionalIdAndActivo(profesionalId, true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServicioResponse buscarPorId(UUID id) {
        Servicio servicio = buscarEntidadPropia(id);
        return toResponse(servicio);
    }

    @Transactional
    public ServicioResponse crear(ServicioRequest request) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional autenticada con ID: " + profesionalId));

        if (servicioRepository.existsByNombreAndProfesionalId(
                request.getNombre(), profesionalId)) {
            throw new IllegalArgumentException(
                    "La profesional ya tiene un servicio con el nombre: " + request.getNombre());
        }

        Servicio servicio = toEntity(request, profesional);
        Servicio guardado = servicioRepository.save(servicio);
        return toResponse(guardado);
    }

    @Transactional
    public ServicioResponse actualizar(UUID id, ServicioRequest request) {
        Servicio servicio = buscarEntidadPropia(id);
        UUID profesionalId = servicio.getProfesional().getId();

        // Si cambió el nombre, verificar que no esté duplicado para esa profesional
        if (!servicio.getNombre().equals(request.getNombre())
                && servicioRepository.existsByNombreAndProfesionalId(
                request.getNombre(), profesionalId)) {
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
        buscarEntidadPropia(id);

        int filasAfectadas = servicioRepository.cambiarEstado(id, activo);
        if (filasAfectadas == 0) {
            throw new EntityNotFoundException(
                    "No se pudo actualizar el estado del servicio con ID: " + id);
        }
    }

    @Transactional
    public void actualizarPrecio(UUID id, java.math.BigDecimal nuevoPrecio) {
        buscarEntidadPropia(id);

        if (nuevoPrecio == null || nuevoPrecio.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        int filasAfectadas = servicioRepository.actualizarPrecio(id, nuevoPrecio);
        if (filasAfectadas == 0) {
            throw new EntityNotFoundException(
                    "No se pudo actualizar el precio del servicio con ID: " + id);
        }
    }

    private Servicio buscarEntidadPropia(UUID id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el servicio con ID: " + id));
        validarPerteneceAProfesionalAutenticada(servicio);
        return servicio;
    }

    private void validarPerteneceAProfesionalAutenticada(Servicio servicio) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        if (!servicio.getProfesional().getId().equals(profesionalId)) {
            throw new AccesoNoAutorizadoException("No se encontró el servicio con ID: " + servicio.getId());
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
