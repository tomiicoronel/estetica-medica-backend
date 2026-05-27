package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.BloqueoAgendaRequest;
import com.estetica.estetica.dto.response.BloqueoAgendaResponse;
import com.estetica.estetica.exception.AccesoNoAutorizadoException;
import com.estetica.estetica.model.BloqueoAgenda;
import com.estetica.estetica.model.EstadoTurno;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.repository.BloqueoAgendaRepository;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.estetica.estetica.repository.TurnoRepository;
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
public class BloqueoAgendaService {

    private final BloqueoAgendaRepository bloqueoAgendaRepository;
    private final ProfesionalRepository profesionalRepository;
    private final TurnoRepository turnoRepository;
    private final ProfesionalAutenticadaService profesionalAutenticadaService;

    @Transactional
    public BloqueoAgendaResponse crear(BloqueoAgendaRequest request) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional autenticada con ID: " + profesionalId));

        validarFechas(request.getFechaInicio(), request.getFechaFin());
        validarSinSolapamiento(profesionalId, request.getFechaInicio(), request.getFechaFin());
        validarSinTurnosAgendados(profesionalId, request.getFechaInicio(), request.getFechaFin());

        BloqueoAgenda bloqueoAgenda = BloqueoAgenda.builder()
                .profesional(profesional)
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .motivo(normalizarMotivo(request.getMotivo()))
                .build();

        BloqueoAgenda guardado = bloqueoAgendaRepository.save(bloqueoAgenda);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<BloqueoAgendaResponse> listarPorProfesional() {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        return bloqueoAgendaRepository.findByProfesionalIdOrderByFechaInicioAsc(profesionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BloqueoAgendaResponse buscarPorId(UUID id) {
        BloqueoAgenda bloqueoAgenda = buscarEntidadPropia(id);
        return toResponse(bloqueoAgenda);
    }

    @Transactional
    public BloqueoAgendaResponse actualizar(UUID id, BloqueoAgendaRequest request) {
        BloqueoAgenda bloqueoAgenda = buscarEntidadPropia(id);
        UUID profesionalId = bloqueoAgenda.getProfesional().getId();

        validarFechas(request.getFechaInicio(), request.getFechaFin());
        validarSinSolapamientoExcluyendoId(profesionalId, id, request.getFechaInicio(), request.getFechaFin());
        validarSinTurnosAgendados(profesionalId, request.getFechaInicio(), request.getFechaFin());

        bloqueoAgenda.setFechaInicio(request.getFechaInicio());
        bloqueoAgenda.setFechaFin(request.getFechaFin());
        bloqueoAgenda.setMotivo(normalizarMotivo(request.getMotivo()));

        BloqueoAgenda actualizado = bloqueoAgendaRepository.saveAndFlush(bloqueoAgenda);
        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(UUID id) {
        BloqueoAgenda bloqueoAgenda = buscarEntidadPropia(id);
        bloqueoAgendaRepository.delete(bloqueoAgenda);
    }

    private BloqueoAgenda buscarEntidadPropia(UUID id) {
        BloqueoAgenda bloqueoAgenda = bloqueoAgendaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el bloqueo de agenda con ID: " + id));
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        if (!bloqueoAgenda.getProfesional().getId().equals(profesionalId)) {
            throw new AccesoNoAutorizadoException("No se encontró el bloqueo de agenda con ID: " + id);
        }
        return bloqueoAgenda;
    }

    private void validarFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio del bloqueo es obligatoria");
        }
        if (fechaFin == null) {
            throw new IllegalArgumentException("La fecha de fin del bloqueo es obligatoria");
        }
        if (fechaInicio.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de inicio del bloqueo no puede estar en el pasado");
        }
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin del bloqueo debe ser posterior a la fecha de inicio");
        }
    }

    private void validarSinSolapamiento(UUID profesionalId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (bloqueoAgendaRepository.existeSolapamiento(profesionalId, fechaInicio, fechaFin)) {
            throw new IllegalArgumentException(
                    "El bloqueo de agenda se solapa con otro bloqueo existente de la misma profesional");
        }
    }

    private void validarSinSolapamientoExcluyendoId(
            UUID profesionalId,
            UUID bloqueoId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    ) {
        if (bloqueoAgendaRepository.existeSolapamientoExcluyendoId(
                profesionalId, bloqueoId, fechaInicio, fechaFin)) {
            throw new IllegalArgumentException(
                    "El bloqueo de agenda se solapa con otro bloqueo existente de la misma profesional");
        }
    }

    private void validarSinTurnosAgendados(UUID profesionalId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        long cantidadTurnos = turnoRepository.countByProfesionalIdAndFechaHoraGreaterThanEqualAndFechaHoraLessThanAndEstadoNot(
                profesionalId,
                fechaInicio,
                fechaFin,
                EstadoTurno.CANCELADO
        );

        if (cantidadTurnos > 0) {
            throw new IllegalArgumentException(
                    "No se puede crear o actualizar el bloqueo porque existen " + cantidadTurnos
                            + " turno(s) agendado(s) en ese rango. Movelos o cancelalos primero.");
        }
    }

    private String normalizarMotivo(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            return null;
        }
        return motivo.trim();
    }

    private BloqueoAgendaResponse toResponse(BloqueoAgenda bloqueoAgenda) {
        return BloqueoAgendaResponse.builder()
                .id(bloqueoAgenda.getId())
                .profesionalId(bloqueoAgenda.getProfesional().getId())
                .fechaInicio(bloqueoAgenda.getFechaInicio())
                .fechaFin(bloqueoAgenda.getFechaFin())
                .motivo(bloqueoAgenda.getMotivo())
                .creadoEn(bloqueoAgenda.getCreadoEn())
                .actualizadoEn(bloqueoAgenda.getActualizadoEn())
                .build();
    }
}

