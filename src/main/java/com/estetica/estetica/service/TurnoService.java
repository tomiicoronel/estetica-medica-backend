package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.TurnoRequest;
import com.estetica.estetica.dto.response.TurnoResponse;
import com.estetica.estetica.exception.AccesoNoAutorizadoException;
import com.estetica.estetica.model.*;
import com.estetica.estetica.repository.BloqueoAgendaRepository;
import com.estetica.estetica.repository.PacienteRepository;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.estetica.estetica.repository.ServicioRepository;
import com.estetica.estetica.repository.TurnoRepository;
import com.estetica.estetica.security.ProfesionalAutenticadaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final ProfesionalRepository profesionalRepository;
    private final PacienteRepository pacienteRepository;
    private final ServicioRepository servicioRepository;
    private final BloqueoAgendaRepository bloqueoAgendaRepository;
    private final ProfesionalAutenticadaService profesionalAutenticadaService;

    @Transactional
    public TurnoResponse crear(TurnoRequest request) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        // 1. Validar profesional
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional autenticada con ID: " + profesionalId));

        // 2. Validar paciente y que pertenezca a la profesional
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el paciente con ID: " + request.getPacienteId()));

        if (!paciente.getProfesional().getId().equals(profesionalId)) {
            throw new IllegalArgumentException(
                    "El paciente no pertenece a la profesional con ID: " + profesionalId);
        }

        // 3. Validar fecha no pasada (defensa en profundidad; el DTO también lo valida)
        if (request.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del turno no puede ser en el pasado");
        }

        // 4. Validar servicios: no nulos, no duplicados, existencia, pertenencia y que estén activos
        List<UUID> servicioIds = request.getServicioIds();
        if (servicioIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("La lista de servicios no puede contener IDs nulos");
        }

        Set<UUID> servicioIdsUnicos = Set.copyOf(servicioIds);
        if (servicioIdsUnicos.size() != servicioIds.size()) {
            throw new IllegalArgumentException("La lista de servicios no puede contener IDs duplicados");
        }

        List<Servicio> servicios = servicioRepository.findAllById(servicioIdsUnicos);

        if (servicios.size() != servicioIdsUnicos.size()) {
            throw new EntityNotFoundException(
                    "Uno o más servicios no existen. IDs solicitados: " + servicioIds);
        }

        for (Servicio s : servicios) {
            if (!s.getProfesional().getId().equals(profesionalId)) {
                throw new IllegalArgumentException(
                        "El servicio " + s.getId() + " no pertenece a la profesional con ID: " + profesionalId);
            }
            if (Boolean.FALSE.equals(s.getActivo())) {
                throw new IllegalArgumentException(
                        "El servicio '" + s.getNombre() + "' está inactivo y no puede agendarse");
            }
        }

        // 5. Validar que la fecha no esté bloqueada en la agenda de la profesional
        validarFechaNoBloqueada(profesionalId, request.getFechaHora());

        // 6. Construir el turno (sin TurnoServicio todavía, necesitamos el ID)
        Turno turno = Turno.builder()
                .profesional(profesional)
                .paciente(paciente)
                .fechaHora(request.getFechaHora())
                .estado(EstadoTurno.PENDIENTE)
                .observaciones(request.getObservaciones())
                .montoTotal(BigDecimal.ZERO)
                .turnoServicios(new ArrayList<>())
                .build();

        // 7. Crear los TurnoServicio con el precio congelado y calcular monto total
        BigDecimal montoTotal = BigDecimal.ZERO;
        for (Servicio s : servicios) {
            TurnoServicio ts = TurnoServicio.builder()
                    .turno(turno)
                    .servicio(s)
                    .precioMomento(s.getPrecio())
                    .build();
            turno.getTurnoServicios().add(ts);
            montoTotal = montoTotal.add(s.getPrecio());
        }
        turno.setMontoTotal(montoTotal);

        Turno guardado = turnoRepository.save(turno);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public TurnoResponse buscarPorId(UUID id) {
        Turno turno = buscarEntidadPropia(id);
        return toResponse(turno);
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarPorProfesional() {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        return turnoRepository.findByProfesionalId(profesionalId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarPorProfesionalYEstado(EstadoTurno estado) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        return turnoRepository.findByProfesionalIdAndEstado(profesionalId, estado)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarPorProfesionalYRango(LocalDateTime desde, LocalDateTime hasta) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Las fechas 'desde' y 'hasta' son obligatorias");
        }
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha 'hasta' no puede ser anterior a 'desde'");
        }
        return turnoRepository.findByProfesionalIdAndFechaHoraBetween(profesionalId, desde, hasta)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarProximos(LocalDate fecha) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();

        // Si no se indica fecha, se busca el próximo día (a partir de hoy) que tenga al menos
        // un turno y se devuelven todos los turnos de ese día. Si se indica, se devuelven
        // todos los turnos de esa fecha. En ambos casos sin límite fijo de cantidad.
        LocalDate dia = fecha;
        if (dia == null) {
            LocalDateTime hoy = LocalDate.now().atStartOfDay();
            dia = turnoRepository
                    .findFirstByProfesionalIdAndFechaHoraGreaterThanEqualOrderByFechaHoraAsc(profesionalId, hoy)
                    .map(turno -> turno.getFechaHora().toLocalDate())
                    .orElse(null);
            if (dia == null) {
                return List.of();
            }
        }

        LocalDateTime desde = dia.atStartOfDay();
        LocalDateTime hasta = dia.plusDays(1).atStartOfDay();
        return turnoRepository
                .findByProfesionalIdAndFechaHoraGreaterThanEqualAndFechaHoraLessThanOrderByFechaHoraAsc(profesionalId, desde, hasta)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarPorPaciente(UUID pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el paciente con ID: " + pacienteId));
        validarPacientePerteneceAProfesionalAutenticada(paciente);
        return turnoRepository.findByPacienteId(pacienteId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public TurnoResponse cambiarEstado(UUID id, EstadoTurno nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio");
        }
        Turno turno = buscarEntidadPropia(id);

        EstadoTurno actual = turno.getEstado();
        if (!esTransicionValida(actual, nuevoEstado)) {
            throw new IllegalArgumentException(
                    "Transición de estado inválida: " + actual + " → " + nuevoEstado);
        }

        turno.setEstado(nuevoEstado);
        Turno actualizado = turnoRepository.saveAndFlush(turno);
        return toResponse(actualizado);
    }

    private boolean esTransicionValida(EstadoTurno actual, EstadoTurno nuevo) {
        if (actual.equals(nuevo)) {
            return false;
        }
        return switch (actual) {
            case PENDIENTE -> nuevo.equals(EstadoTurno.CONFIRMADO) || nuevo.equals(EstadoTurno.CANCELADO);
            case CONFIRMADO -> nuevo.equals(EstadoTurno.REALIZADO) || nuevo.equals(EstadoTurno.CANCELADO);
            // REALIZADO y CANCELADO son estados finales
            default -> false;
        };
    }

    Turno buscarEntidadPropia(UUID id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el turno con ID: " + id));
        validarTurnoPerteneceAProfesionalAutenticada(turno);
        return turno;
    }

    void validarTurnoPerteneceAProfesionalAutenticada(Turno turno) {
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

    private void validarFechaNoBloqueada(UUID profesionalId, LocalDateTime fechaHora) {
        bloqueoAgendaRepository
                .findFirstByProfesionalIdAndFechaInicioLessThanEqualAndFechaFinGreaterThan(
                        profesionalId,
                        fechaHora,
                        fechaHora
                )
                .ifPresent(bloqueo -> {
                    String mensaje = "La fecha del turno está bloqueada en la agenda de la profesional";
                    if (bloqueo.getMotivo() != null && !bloqueo.getMotivo().isBlank()) {
                        mensaje += ". Motivo: " + bloqueo.getMotivo();
                    }
                    throw new IllegalArgumentException(mensaje);
                });
    }

    private TurnoResponse toResponse(Turno turno) {
        List<TurnoResponse.TurnoServicioResponse> servicios = turno.getTurnoServicios()
                .stream()
                .map(ts -> TurnoResponse.TurnoServicioResponse.builder()
                        .servicioId(ts.getServicio().getId())
                        .nombre(ts.getServicio().getNombre())
                        .precioMomento(ts.getPrecioMomento())
                        .build())
                .collect(Collectors.toList());

        return TurnoResponse.builder()
                .id(turno.getId())
                .profesionalId(turno.getProfesional().getId())
                .pacienteId(turno.getPaciente().getId())
                .fechaHora(turno.getFechaHora())
                .estado(turno.getEstado())
                .montoTotal(turno.getMontoTotal())
                .observaciones(turno.getObservaciones())
                .servicios(servicios)
                .creadoEn(turno.getCreadoEn())
                .actualizadoEn(turno.getActualizadoEn())
                .build();
    }
}
