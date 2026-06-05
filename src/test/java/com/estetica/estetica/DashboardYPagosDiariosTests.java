package com.estetica.estetica;

import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.model.RolUsuario;
import com.estetica.estetica.repository.PacienteRepository;
import com.estetica.estetica.repository.PagoRepository;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.estetica.estetica.repository.ServicioRepository;
import com.estetica.estetica.repository.TurnoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DashboardYPagosDiariosTests {

    private static final String EMAIL = "dashboard.test@estetica.local";
    private static final String PASSWORD = "Password123!";

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ProfesionalRepository profesionalRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private TurnoRepository turnoRepository;
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void prepararProfesional() {
        limpiarDatos();
        Profesional profesional = profesionalRepository.findByEmail(EMAIL)
                .orElseGet(() -> Profesional.builder()
                        .nombre("Dashboard")
                        .apellido("Test")
                        .email(EMAIL)
                        .telefono("3510000040")
                        .especialidad("Test")
                        .build());
        profesional.setPassword(passwordEncoder.encode(PASSWORD));
        profesional.setDebeCambiarPassword(false);
        profesional.setRol(RolUsuario.PROFESIONAL);
        profesionalRepository.saveAndFlush(profesional);
    }

    @AfterEach
    void limpiarDatosFinal() {
        limpiarDatos();
    }

    @Test
    void dashboardYPagosDiariosCalculanPorDiaConAislamiento() throws Exception {
        String token = login();
        LocalDate dia = LocalDate.now().plusMonths(2);

        String pacienteId = extraer(postJson("/api/pacientes", token, """
                {
                  "nombre": "Paciente",
                  "apellido": "Dashboard",
                  "dniCuit": "40111222",
                  "telefono": "3510000001"
                }
                """), "id");

        String servicioId = extraer(postJson("/api/servicios", token, """
                {
                  "nombre": "Servicio dashboard",
                  "descripcion": "Servicio para probar el dashboard por día",
                  "precio": 10000.00
                }
                """), "id");

        String turnoId = extraer(postJson("/api/turnos", token, """
                {
                  "pacienteId": "%s",
                  "fechaHora": "%sT12:00:00",
                  "servicioIds": ["%s"],
                  "observaciones": "Turno para dashboard"
                }
                """.formatted(pacienteId, dia, servicioId)), "id");

        HttpResponse<String> registrarPago = postJson("/api/turnos/" + turnoId + "/pagos", token, """
                {
                  "metodo": "EFECTIVO",
                  "monto": 6000.00,
                  "fecha": "%sT12:30:00"
                }
                """.formatted(dia));
        assertThat(registrarPago.statusCode()).isEqualTo(201);

        // Dashboard del día: 1 turno, 0 realizados aún, 1 paciente activo, 6000 recaudado.
        JsonNode dashboard = objectMapper.readTree(getAuth("/api/dashboard?fecha=" + dia, token).body());
        assertThat(dashboard.get("cantidadTurnos").asLong()).isEqualTo(1);
        assertThat(dashboard.get("cantidadTurnosRealizados").asLong()).isEqualTo(0);
        assertThat(dashboard.get("pacientesActivos").asLong()).isEqualTo(1);
        assertThat(dashboard.get("totalRecaudado").decimalValue()).isEqualByComparingTo(new BigDecimal("6000"));

        // Marcar el turno como REALIZADO: PENDIENTE -> CONFIRMADO -> REALIZADO.
        assertThat(patchAuth("/api/turnos/" + turnoId + "/estado?nuevoEstado=CONFIRMADO", token).statusCode()).isEqualTo(200);
        assertThat(patchAuth("/api/turnos/" + turnoId + "/estado?nuevoEstado=REALIZADO", token).statusCode()).isEqualTo(200);

        JsonNode dashboardLuego = objectMapper.readTree(getAuth("/api/dashboard?fecha=" + dia, token).body());
        assertThat(dashboardLuego.get("cantidadTurnos").asLong()).isEqualTo(1);
        assertThat(dashboardLuego.get("cantidadTurnosRealizados").asLong()).isEqualTo(1);

        // Resumen diario de pagos del día: total 6000 y 1 pago.
        JsonNode resumenDia = objectMapper.readTree(getAuth("/api/pagos/resumen-diario?fecha=" + dia, token).body());
        assertThat(resumenDia.get("totalRecaudado").decimalValue()).isEqualByComparingTo(new BigDecimal("6000"));
        assertThat(resumenDia.get("cantidadPagos").asInt()).isEqualTo(1);
        assertThat(resumenDia.get("pagos")).hasSize(1);
        assertThat(resumenDia.get("pagos").get(0).get("monto").decimalValue()).isEqualByComparingTo(new BigDecimal("6000"));

        // Otro día (anterior): no hay pagos.
        JsonNode resumenOtroDia = objectMapper.readTree(
                getAuth("/api/pagos/resumen-diario?fecha=" + dia.minusDays(1), token).body());
        assertThat(resumenOtroDia.get("totalRecaudado").decimalValue()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(resumenOtroDia.get("cantidadPagos").asInt()).isEqualTo(0);
        assertThat(resumenOtroDia.get("pagos")).isEmpty();

        // Próximos turnos por fecha explícita.
        JsonNode proximosPorFecha = objectMapper.readTree(getAuth("/api/turnos/proximos?fecha=" + dia, token).body());
        assertThat(proximosPorFecha).hasSize(1);
        assertThat(proximosPorFecha.get(0).get("id").asText()).isEqualTo(turnoId);

        // Próximos turnos sin fecha: el siguiente día con turnos es 'dia'.
        JsonNode proximosSinFecha = objectMapper.readTree(getAuth("/api/turnos/proximos", token).body());
        assertThat(proximosSinFecha).hasSize(1);
        assertThat(proximosSinFecha.get(0).get("id").asText()).isEqualTo(turnoId);
    }

    private String login() throws Exception {
        HttpResponse<String> respuesta = postJson("/api/auth/login", null, """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(EMAIL, PASSWORD));
        assertThat(respuesta.statusCode()).isEqualTo(200);
        return objectMapper.readTree(respuesta.body()).get("token").asText();
    }

    private void limpiarDatos() {
        profesionalRepository.findByEmail(EMAIL).ifPresent(profesional -> {
            UUID profId = profesional.getId();
            pagoRepository.deleteAll(pagoRepository.findByTurno_Profesional_Id(profId));
            pagoRepository.flush();
            turnoRepository.deleteAll(turnoRepository.findByProfesionalId(profId));
            turnoRepository.flush();
            servicioRepository.deleteAll(servicioRepository.findByProfesionalId(profId));
            pacienteRepository.deleteAll(pacienteRepository.findByProfesionalId(profId));
            servicioRepository.flush();
            pacienteRepository.flush();
            profesionalRepository.delete(profesional);
            profesionalRepository.flush();
        });
    }

    private String extraer(HttpResponse<String> respuesta, String campo) throws Exception {
        JsonNode json = objectMapper.readTree(respuesta.body());
        assertThat(json.hasNonNull(campo))
                .as("La respuesta debe incluir '%s'. Body: %s", campo, respuesta.body())
                .isTrue();
        return json.get(campo).asText();
    }

    private HttpResponse<String> postJson(String path, String token, String body) throws Exception {
        HttpRequest.Builder builder = request(path)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
        agregarAuthorization(builder, token);
        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getAuth(String path, String token) throws Exception {
        HttpRequest.Builder builder = request(path).GET();
        agregarAuthorization(builder, token);
        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> patchAuth(String path, String token) throws Exception {
        HttpRequest.Builder builder = request(path).method("PATCH", HttpRequest.BodyPublishers.noBody());
        agregarAuthorization(builder, token);
        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest.Builder request(String path) {
        return HttpRequest.newBuilder(URI.create("http://localhost:" + port + path));
    }

    private void agregarAuthorization(HttpRequest.Builder builder, String token) {
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
    }
}
