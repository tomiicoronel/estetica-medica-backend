package com.estetica.estetica.repository;

import com.estetica.estetica.model.BloqueoAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BloqueoAgendaRepository extends JpaRepository<BloqueoAgenda, UUID> {

    List<BloqueoAgenda> findByProfesionalId(UUID profesionalId);

    List<BloqueoAgenda> findByProfesionalIdOrderByFechaInicioAsc(UUID profesionalId);

    boolean existsByProfesionalIdAndFechaInicioLessThanAndFechaFinGreaterThan(
            UUID profesionalId,
            LocalDateTime fechaFin,
            LocalDateTime fechaInicio
    );

    Optional<BloqueoAgenda> findFirstByProfesionalIdAndFechaInicioLessThanEqualAndFechaFinGreaterThan(
            UUID profesionalId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );

    @Query("""
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM BloqueoAgenda b
            WHERE b.profesional.id = :profesionalId
              AND b.fechaInicio < :fechaFin
              AND b.fechaFin > :fechaInicio
            """)
    boolean existeSolapamiento(
            @Param("profesionalId") UUID profesionalId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("""
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM BloqueoAgenda b
            WHERE b.profesional.id = :profesionalId
              AND b.id <> :bloqueoId
              AND b.fechaInicio < :fechaFin
              AND b.fechaFin > :fechaInicio
            """)
    boolean existeSolapamientoExcluyendoId(
            @Param("profesionalId") UUID profesionalId,
            @Param("bloqueoId") UUID bloqueoId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}


