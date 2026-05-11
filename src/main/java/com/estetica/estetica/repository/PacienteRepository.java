package com.estetica.estetica.repository;

import com.estetica.estetica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

    List<Paciente> findByProfesionalId(UUID profesionalId);

    boolean existsByDniCuitAndProfesionalId(String dniCuit, UUID profesionalId);

    List<Paciente> findByProfesionalIdAndActivo(UUID profesionalId, Boolean activo);

    @Modifying
    @Query("UPDATE Paciente p SET p.activo = :activo, p.actualizadoEn = CURRENT_TIMESTAMP WHERE p.id = :id")
    int cambiarEstado(@Param("id") UUID id, @Param("activo") Boolean activo);
}
