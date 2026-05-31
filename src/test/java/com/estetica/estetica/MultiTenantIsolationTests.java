package com.estetica.estetica;

import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MultiTenantIsolationTests {

    private static final String PASSWORD_INICIAL = "Password123!";
    private static final String EMAIL_ANA = "ana.lopez@estetica.local";
    private static final String EMAIL_MARIA = "maria.gonzalez@estetica.local";

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void habilitarProfesionalesParaTest() {
        actualizarProfesionalSeeder(EMAIL_ANA, false);
        actualizarProfesionalSeeder(EMAIL_MARIA, false);
    }

    @AfterEach
    void restaurarProfesionalesSeeder() {
        actualizarProfesionalSeeder(EMAIL_ANA, true);
        actualizarProfesionalSeeder(EMAIL_MARIA, true);
    }

    @Test
    void mariaNoPuedeAccederARecursosDeAna() throws Exception {
        HttpResponse<String> loginAna = postJson("/api/auth/login", null, """
                                {
                                  "email": "ana.lopez@estetica.local",
                                  "password": "Password123!"
                                }
                                """);
        int paso1 = loginAna.statusCode();
        String tokenAna = extraer(loginAna, "token");

        String sufijo = String.valueOf(System.currentTimeMillis());
        HttpResponse<String> crearPacienteAna = postJson("/api/pacientes", tokenAna, """
                                {
                                  "nombre": "Paciente",
                                  "apellido": "Aislamiento",
                                  "dniCuit": "%s",
                                  "telefono": "3510000000"
                                }
                                """.formatted(sufijo));
        int paso2 = crearPacienteAna.statusCode();
        String pacienteAnaId = extraer(crearPacienteAna, "id");

        HttpResponse<String> crearServicioAna = postJson("/api/servicios", tokenAna, """
                                {
                                  "nombre": "Servicio aislamiento %s",
                                  "descripcion": "Servicio para probar aislamiento multi-tenant",
                                  "precio": 50000.00
                                }
                                """.formatted(sufijo));
        int paso3 = crearServicioAna.statusCode();
        String servicioAnaId = extraer(crearServicioAna, "id");

        String fechaTurno = LocalDateTime.now().plusYears(3).withNano(0).toString();
        HttpResponse<String> crearTurnoAna = postJson("/api/turnos", tokenAna, """
                                {
                                  "pacienteId": "%s",
                                  "fechaHora": "%s",
                                  "servicioIds": ["%s"],
                                  "observaciones": "Turno para probar aislamiento"
                                }
                                """.formatted(pacienteAnaId, fechaTurno, servicioAnaId));
        int paso4 = crearTurnoAna.statusCode();
        String turnoAnaId = extraer(crearTurnoAna, "id");

        HttpResponse<String> loginMaria = postJson("/api/auth/login", null, """
                                {
                                  "email": "maria.gonzalez@estetica.local",
                                  "password": "Password123!"
                                }
                                """);
        int paso5 = loginMaria.statusCode();
        String tokenMaria = extraer(loginMaria, "token");

        HttpResponse<String> mariaVePacienteAna = getAuth("/api/pacientes/" + pacienteAnaId, tokenMaria);
        int paso6 = mariaVePacienteAna.statusCode();

        HttpResponse<String> mariaVeTurnoAna = getAuth("/api/turnos/" + turnoAnaId, tokenMaria);
        int paso7 = mariaVeTurnoAna.statusCode();

        HttpResponse<String> mariaActualizaServicioAna = patchAuth(
                "/api/servicios/" + servicioAnaId + "/precio?nuevoPrecio=1", tokenMaria);
        int paso8 = mariaActualizaServicioAna.statusCode();

        HttpResponse<String> mariaListaPacientes = getAuth("/api/pacientes", tokenMaria);
        int paso9 = mariaListaPacientes.statusCode();
        String bodyPaso9 = mariaListaPacientes.body() != null ? mariaListaPacientes.body() : "";

        System.out.println("Paso 1 login Ana: " + paso1);
        System.out.println("Paso 2 crear paciente Ana: " + paso2 + " | pacienteId=" + pacienteAnaId);
        System.out.println("Paso 3 crear servicio Ana: " + paso3 + " | servicioId=" + servicioAnaId);
        System.out.println("Paso 4 crear turno Ana: " + paso4 + " | turnoId=" + turnoAnaId);
        System.out.println("Paso 5 login María: " + paso5 + " | tokenDistinto=" + !tokenAna.equals(tokenMaria));
        System.out.println("Paso 6 María GET paciente de Ana: " + paso6);
        System.out.println("Paso 7 María GET turno de Ana: " + paso7);
        System.out.println("Paso 8 María PATCH precio servicio de Ana: " + paso8);
        System.out.println("Paso 9 María GET pacientes: " + paso9 + " | contienePacienteAna=" + bodyPaso9.contains(pacienteAnaId));

        assertThat(paso1).isEqualTo(200);
        assertThat(paso2).isEqualTo(201);
        assertThat(paso3).isEqualTo(201);
        assertThat(paso4).isEqualTo(201);
        assertThat(paso5).isEqualTo(200);
        assertThat(tokenAna).isNotEqualTo(tokenMaria);
        assertThat(paso6).isEqualTo(404);
        assertThat(paso7).isEqualTo(404);
        assertThat(paso8).isEqualTo(404);
        assertThat(paso9).isEqualTo(200);
        assertThat(bodyPaso9).doesNotContain(pacienteAnaId);
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

    private String extraer(HttpResponse<String> result, String fieldName) throws Exception {
        JsonNode json = objectMapper.readTree(result.body());
        assertThat(json.hasNonNull(fieldName))
                .as("La respuesta debe incluir el campo '%s'. Body: %s", fieldName, result.body())
                .isTrue();
        return json.get(fieldName).asText();
    }

    private void actualizarProfesionalSeeder(String email, boolean debeCambiarPassword) {
        Profesional profesional = profesionalRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("No existe la profesional del seeder: " + email));
        profesional.setPassword(passwordEncoder.encode(PASSWORD_INICIAL));
        profesional.setDebeCambiarPassword(debeCambiarPassword);
        profesionalRepository.saveAndFlush(profesional);
    }
}



