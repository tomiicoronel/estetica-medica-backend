package com.estetica.estetica.security;

import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class CambioPasswordObligatorioFilter extends OncePerRequestFilter {

    private static final String MENSAJE_CAMBIO_PASSWORD = "Debe cambiar su contraseña inicial antes de usar el sistema";

    private final ProfesionalRepository profesionalRepository;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (esRutaExenta(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!estaAutenticado(authentication)) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID profesionalId = obtenerProfesionalId(authentication);
        if (profesionalRepository.existsByIdAndDebeCambiarPasswordTrue(profesionalId)) {
            escribirBloqueo(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean esRutaExenta(HttpServletRequest request) {
        String metodo = request.getMethod();
        String path = request.getServletPath();

        if (HttpMethod.OPTIONS.matches(metodo)) {
            return true;
        }
        if (HttpMethod.POST.matches(metodo)
                && ("/api/auth/login".equals(path)
                || "/api/auth/cambiar-password".equals(path)
                || "/api/auth/logout".equals(path))) {
            return true;
        }

        return "/swagger-ui.html".equals(path)
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs");
    }

    private boolean estaAutenticado(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private UUID obtenerProfesionalId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UUID profesionalId) {
            return profesionalId;
        }
        if (principal instanceof String principalTexto) {
            return UUID.fromString(principalTexto);
        }
        throw new IllegalStateException("La identidad autenticada no corresponde a una profesional válida");
    }

    private void escribirBloqueo(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Acceso denegado")
                .mensaje(MENSAJE_CAMBIO_PASSWORD)
                .build();
        objectMapper.writeValue(response.getWriter(), body);
    }
}
