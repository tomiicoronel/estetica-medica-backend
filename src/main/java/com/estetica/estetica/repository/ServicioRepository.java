package com.estetica.estetica.repository;

import com.estetica.estetica.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, UUID> {

    List<Servicio> findByProfesionalId(UUID profesionalId);

    List<Servicio> findByProfesionalIdAndActivo(UUID profesionalId, Boolean activo);

    boolean existsByNombreAndProfesionalId(String nombre, UUID profesionalId);

    @Modifying
    @Query("UPDATE Servicio s SET s.precio = :nuevoPrecio, s.actualizadoEn = CURRENT_TIMESTAMP WHERE s.id = :id")
    int actualizarPrecio(@Param("id") UUID id, @Param("nuevoPrecio") BigDecimal nuevoPrecio);

    @Modifying
    @Query("UPDATE Servicio s SET s.activo = :activo, s.actualizadoEn = CURRENT_TIMESTAMP WHERE s.id = :id")
    int cambiarEstado(@Param("id") UUID id, @Param("activo") Boolean activo);
}
