package com.estetica.estetica;

import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.model.RolUsuario;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTests {

    private static final String ADMIN_EMAIL = "admin.test@estetica.local";
    private static final String ADMIN_PASSWORD_INICIAL = "Password123!";
    private static final String ADMIN_PASSWORD_NUEVA = "AdminNueva123!";
    private static final String PROFESIONAL_EMAIL = "admin.creada@estetica.local";
    private static final String PROFESIONAL_EMAIL_EDITADO = "admin.editada@estetica.local";
    private static final String PROFESIONAL_EMAIL_DUPLICADO = "admin.duplicada@estetica.local";
    private static final String PROFESIONAL_PASSWORD_INICIAL = "Profesional123!";
    private static final String PROFESIONAL_PASSWORD_NUEVA = "ProfesionalNueva123!";
    private static final String PROFESIONAL_PASSWORD_RESETEADA = "ProfesionalReset123!";

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void prepararDatos() {
        eliminarProfesionalCreada();
        prepararAdmin(true, ADMIN_PASSWORD_INICIAL);
    }

    @AfterEach
    void limpiarDatos() {
        eliminarProfesionalCreada();
        prepararAdmin(true, ADMIN_PASSWORD_INICIAL);
    }

    @Test
    void profesionalNoPuedeAccederAEndpointsAdminYAdminGestionaProfesionales() throws Exception {
        HttpResponse<String> loginAdminInicial = login(ADMIN_EMAIL, ADMIN_PASSWORD_INICIAL);
        JsonNode loginAdminInicialBody = objectMapper.readTree(loginAdminInicial.body());
        String tokenAdminInicial = loginAdminInicialBody.get("token").asText();

        assertThat(loginAdminInicial.statusCode()).isEqualTo(200);
        assertThat(loginAdminInicialBody.get("rol").asText()).isEqualTo(RolUsuario.ADMIN.name());
        assertThat(loginAdminInicialBody.get("debeCambiarPassword").asBoolean()).isTrue();

        HttpResponse<String> cambiarPasswordAdmin = postJson("/api/auth/cambiar-password", tokenAdminInicial, """
                {
                  "passwordActual": "%s",
                  "passwordNueva": "%s"
                }
                """.formatted(ADMIN_PASSWORD_INICIAL, ADMIN_PASSWORD_NUEVA));
        assertThat(cambiarPasswordAdmin.statusCode()).isEqualTo(204);

        HttpResponse<String> loginAdminFinal = login(ADMIN_EMAIL, ADMIN_PASSWORD_NUEVA);
        JsonNode loginAdminFinalBody = objectMapper.readTree(loginAdminFinal.body());
        String tokenAdmin = loginAdminFinalBody.get("token").asText();

        assertThat(loginAdminFinal.statusCode()).isEqualTo(200);
        assertThat(loginAdminFinalBody.get("rol").asText()).isEqualTo(RolUsuario.ADMIN.name());
        assertThat(loginAdminFinalBody.get("debeCambiarPassword").asBoolean()).isFalse();

        HttpResponse<String> crearProfesional = postJson("/api/admin/profesionales", tokenAdmin, """
                {
                  "nombre": "Profesional",
                  "apellido": "AdminTest",
                  "email": "%s",
                  "telefono": "3510000020",
                  "especialidad": "Test",
                  "password": "%s"
                }
                """.formatted(PROFESIONAL_EMAIL, PROFESIONAL_PASSWORD_INICIAL));
        JsonNode crearProfesionalBody = objectMapper.readTree(crearProfesional.body());
        String profesionalId = crearProfesionalBody.get("id").asText();

        assertThat(crearProfesional.statusCode()).isEqualTo(201);
        assertThat(crearProfesionalBody.has("password")).isFalse();
        Profesional profesionalCreada = profesionalRepository.findByEmail(PROFESIONAL_EMAIL).orElseThrow();
        String passwordHashOriginal = profesionalCreada.getPassword();
        assertThat(profesionalCreada.getRol()).isEqualTo(RolUsuario.PROFESIONAL);
        assertThat(profesionalCreada.isDebeCambiarPassword()).isTrue();

        HttpResponse<String> crearProfesionalDuplicada = postJson("/api/admin/profesionales", tokenAdmin, """
                {
                  "nombre": "Profesional",
                  "apellido": "Duplicada",
                  "email": "%s",
                  "telefono": "3510000022",
                  "especialidad": "Test",
                  "password": "%s"
                }
                """.formatted(PROFESIONAL_EMAIL_DUPLICADO, PROFESIONAL_PASSWORD_INICIAL));
        assertThat(crearProfesionalDuplicada.statusCode()).isEqualTo(201);

        HttpResponse<String> editarProfesional = putJson("/api/admin/profesionales/" + profesionalId, tokenAdmin, """
                {
                  "nombre": "Profesional Editada",
                  "apellido": "AdminTest Editada",
                  "email": "%s",
                  "telefono": "3510000099",
                  "especialidad": "Especialidad editada"
                }
                """.formatted(PROFESIONAL_EMAIL_EDITADO));
        JsonNode editarProfesionalBody = objectMapper.readTree(editarProfesional.body());

        assertThat(editarProfesional.statusCode()).isEqualTo(200);
        assertThat(editarProfesionalBody.get("nombre").asText()).isEqualTo("Profesional Editada");
        assertThat(editarProfesionalBody.get("email").asText()).isEqualTo(PROFESIONAL_EMAIL_EDITADO);
        assertThat(editarProfesionalBody.has("password")).isFalse();
        Profesional profesionalEditada = profesionalRepository.findByEmail(PROFESIONAL_EMAIL_EDITADO).orElseThrow();
        assertThat(profesionalEditada.getRol()).isEqualTo(RolUsuario.PROFESIONAL);
        assertThat(profesionalEditada.getPassword()).isEqualTo(passwordHashOriginal);

        HttpResponse<String> editarConEmailDuplicado = putJson("/api/admin/profesionales/" + profesionalId, tokenAdmin, """
                {
                  "nombre": "Profesional Editada",
                  "apellido": "AdminTest Editada",
                  "email": "%s",
                  "telefono": "3510000099",
                  "especialidad": "Especialidad editada"
                }
                """.formatted(PROFESIONAL_EMAIL_DUPLICADO));
        assertThat(editarConEmailDuplicado.statusCode()).isEqualTo(400);

        HttpResponse<String> loginProfesionalInicial = login(PROFESIONAL_EMAIL_EDITADO, PROFESIONAL_PASSWORD_INICIAL);
        JsonNode loginProfesionalInicialBody = objectMapper.readTree(loginProfesionalInicial.body());
        String tokenProfesionalInicial = loginProfesionalInicialBody.get("token").asText();

        assertThat(loginProfesionalInicial.statusCode()).isEqualTo(200);
        assertThat(loginProfesionalInicialBody.get("rol").asText()).isEqualTo(RolUsuario.PROFESIONAL.name());
        assertThat(loginProfesionalInicialBody.get("debeCambiarPassword").asBoolean()).isTrue();

        HttpResponse<String> cambiarPasswordProfesional = postJson("/api/auth/cambiar-password", tokenProfesionalInicial, """
                {
                  "passwordActual": "%s",
                  "passwordNueva": "%s"
                }
                """.formatted(PROFESIONAL_PASSWORD_INICIAL, PROFESIONAL_PASSWORD_NUEVA));
        assertThat(cambiarPasswordProfesional.statusCode()).isEqualTo(204);

        HttpResponse<String> loginProfesionalFinal = login(PROFESIONAL_EMAIL_EDITADO, PROFESIONAL_PASSWORD_NUEVA);
        JsonNode loginProfesionalFinalBody = objectMapper.readTree(loginProfesionalFinal.body());
        String tokenProfesional = loginProfesionalFinalBody.get("token").asText();

        assertThat(loginProfesionalFinal.statusCode()).isEqualTo(200);
        assertThat(loginProfesionalFinalBody.get("rol").asText()).isEqualTo(RolUsuario.PROFESIONAL.name());
        assertThat(loginProfesionalFinalBody.get("debeCambiarPassword").asBoolean()).isFalse();

        HttpResponse<String> accesoAdminComoProfesional = getAuth("/api/admin/profesionales", tokenProfesional);
        assertThat(accesoAdminComoProfesional.statusCode()).isEqualTo(403);

        HttpResponse<String> editarComoProfesional = putJson("/api/admin/profesionales/" + profesionalId, tokenProfesional, """
                {
                  "nombre": "Intento",
                  "apellido": "Sin Permiso",
                  "email": "sin.permiso@estetica.local",
                  "telefono": "3510000000",
                  "especialidad": "No autorizada"
                }
                """);
        assertThat(editarComoProfesional.statusCode()).isEqualTo(403);

        HttpResponse<String> resetearComoProfesional = postJson(
                "/api/admin/profesionales/" + profesionalId + "/resetear-password", tokenProfesional, """
                {
                  "passwordNueva": "%s"
                }
                """.formatted(PROFESIONAL_PASSWORD_RESETEADA));
        assertThat(resetearComoProfesional.statusCode()).isEqualTo(403);

        HttpResponse<String> listarComoAdmin = getAuth("/api/admin/profesionales", tokenAdmin);
        assertThat(listarComoAdmin.statusCode()).isEqualTo(200);

        HttpResponse<String> resetearComoAdmin = postJson(
                "/api/admin/profesionales/" + profesionalId + "/resetear-password", tokenAdmin, """
                {
                  "passwordNueva": "%s"
                }
                """.formatted(PROFESIONAL_PASSWORD_RESETEADA));
        assertThat(resetearComoAdmin.statusCode()).isEqualTo(204);

        Profesional profesionalReseteada = profesionalRepository.findByEmail(PROFESIONAL_EMAIL_EDITADO).orElseThrow();
        assertThat(passwordEncoder.matches(PROFESIONAL_PASSWORD_RESETEADA, profesionalReseteada.getPassword())).isTrue();
        assertThat(profesionalReseteada.isDebeCambiarPassword()).isTrue();

        HttpResponse<String> loginProfesionalReseteada = login(PROFESIONAL_EMAIL_EDITADO, PROFESIONAL_PASSWORD_RESETEADA);
        JsonNode loginProfesionalReseteadaBody = objectMapper.readTree(loginProfesionalReseteada.body());
        assertThat(loginProfesionalReseteada.statusCode()).isEqualTo(200);
        assertThat(loginProfesionalReseteadaBody.get("debeCambiarPassword").asBoolean()).isTrue();

        HttpResponse<String> eliminarComoAdmin = deleteAuth("/api/admin/profesionales/" + profesionalId, tokenAdmin);
        assertThat(eliminarComoAdmin.statusCode()).isEqualTo(204);
    }

    private HttpResponse<String> login(String email, String password) throws Exception {
        return postJson("/api/auth/login", null, """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(email, password));
    }

    private void prepararAdmin(boolean debeCambiarPassword, String password) {
        Profesional admin = profesionalRepository.findByEmail(ADMIN_EMAIL)
                .orElseGet(() -> Profesional.builder()
                        .nombre("Admin")
                        .apellido("Test")
                        .email(ADMIN_EMAIL)
                        .telefono("3510000021")
                        .especialidad("Administración")
                        .build());
        admin.setPassword(passwordEncoder.encode(password));
        admin.setDebeCambiarPassword(debeCambiarPassword);
        admin.setRol(RolUsuario.ADMIN);
        profesionalRepository.saveAndFlush(admin);
    }

    private void eliminarProfesionalCreada() {
        profesionalRepository.findByEmail(PROFESIONAL_EMAIL).ifPresent(profesionalRepository::delete);
        profesionalRepository.findByEmail(PROFESIONAL_EMAIL_EDITADO).ifPresent(profesionalRepository::delete);
        profesionalRepository.findByEmail(PROFESIONAL_EMAIL_DUPLICADO).ifPresent(profesionalRepository::delete);
        profesionalRepository.flush();
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

    private HttpResponse<String> putJson(String path, String token, String body) throws Exception {
        HttpRequest.Builder builder = request(path)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body));
        agregarAuthorization(builder, token);
        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteAuth(String path, String token) throws Exception {
        HttpRequest.Builder builder = request(path).DELETE();
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
