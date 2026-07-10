package com.minimarket.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba del comportamiento de autenticacion del usuario: credenciales validas
 * devuelven token; credenciales invalidas devuelven 401.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Login con credenciales validas devuelve 200 y un token JWT")
    void loginValido() throws Exception {
        String body = "{\"username\":\"admin\",\"password\":\"admin123\"}";
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.roles[0]").value("ADMINISTRADOR"));
    }

    @Test
    @DisplayName("Login con contrasena incorrecta devuelve 401")
    void loginInvalido() throws Exception {
        String body = "{\"username\":\"admin\",\"password\":\"claveIncorrecta\"}";
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized());
    }
}
