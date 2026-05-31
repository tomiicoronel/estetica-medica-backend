package com.estetica.estetica.repository;

import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.model.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, UUID> {

    Optional<Profesional> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByIdAndDebeCambiarPasswordTrue(UUID id);

    boolean existsByRol(RolUsuario rol);
}
