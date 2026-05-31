package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.CambioPasswordRequest;
import com.estetica.estetica.dto.request.LoginRequest;
import com.estetica.estetica.dto.response.AuthResponse;
import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.estetica.estetica.security.JwtService;
import com.estetica.estetica.security.ProfesionalAutenticadaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfesionalRepository profesionalRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ProfesionalAutenticadaService profesionalAutenticadaService;

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Profesional profesional = profesionalRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (profesional.getPassword() == null || !passwordEncoder.matches(request.getPassword(), profesional.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        return AuthResponse.builder()
                .token(jwtService.generarToken(profesional))
                .tipoToken("Bearer")
                .debeCambiarPassword(profesional.isDebeCambiarPassword())
                .rol(profesional.getRol().name())
                .build();
    }

    @Transactional
    public void cambiarPassword(CambioPasswordRequest request) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la profesional autenticada con ID: " + profesionalId));

        if (profesional.getPassword() == null
                || !passwordEncoder.matches(request.getPasswordActual(), profesional.getPassword())) {
            throw new BadCredentialsException("La contraseña actual es incorrecta");
        }

        profesional.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
        profesional.setDebeCambiarPassword(false);
        profesionalRepository.save(profesional);
    }
}

