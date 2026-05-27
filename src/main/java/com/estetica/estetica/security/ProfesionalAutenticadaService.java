package com.estetica.estetica.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProfesionalAutenticadaService {

    public UUID obtenerIdProfesionalAutenticada() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new AuthenticationCredentialsNotFoundException("No hay una profesional autenticada en la solicitud");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UUID profesionalId) {
            return profesionalId;
        }
        if (principal instanceof String principalTexto) {
            try {
                return UUID.fromString(principalTexto);
            } catch (IllegalArgumentException ex) {
                throw new AuthenticationCredentialsNotFoundException("La identidad autenticada no tiene un UUID válido");
            }
        }

        throw new AuthenticationCredentialsNotFoundException("La identidad autenticada no corresponde a una profesional válida");
    }
}

