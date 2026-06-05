package com.estetica.estetica.repository;

import com.estetica.estetica.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PagoRepository extends JpaRepository<Pago, UUID> {

    List<Pago> findByTurnoId(UUID turnoId);

    List<Pago> findByTurnoIdOrderByFechaAsc(UUID turnoId);

    List<Pago> findByTurno_Profesional_Id(UUID profesionalId);

    List<Pago> findByTurno_Profesional_IdAndFechaGreaterThanEqualAndFechaLessThanOrderByFechaAsc(
            UUID profesionalId, LocalDateTime desde, LocalDateTime hasta);

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.turno.id = :turnoId")
    BigDecimal sumMontoByTurnoId(@Param("turnoId") UUID turnoId);

    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p "
            + "WHERE p.turno.profesional.id = :profesionalId AND p.fecha >= :desde AND p.fecha < :hasta")
    BigDecimal sumMontoByProfesionalIdAndFechaRango(
            @Param("profesionalId") UUID profesionalId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);
}


