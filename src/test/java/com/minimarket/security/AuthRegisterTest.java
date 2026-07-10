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

/** Pruebas del registro de usuarios: caso exitoso y usuario duplicado. */
@SpringBootTest
@AutoConfigureMockMvc
class AuthRegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Registrar un usuario nuevo devuelve 201")
    void registroExitoso() throws Exception {
        String body = "{\"username\":\"nuevoCliente\",\"email\":\"nuevo@minimarket.cl\",\"password\":\"clave123\"}";
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    @DisplayName("Registrar un usuario que ya existe devuelve 400")
    void registroDuplicado() throws Exception {
        // 'admin' ya existe (cargado por DataInitializer)
        String body = "{\"username\":\"admin\",\"email\":\"admin2@minimarket.cl\",\"password\":\"clave123\"}";
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro con datos invalidos devuelve 400")
    void registroInvalido() throws Exception {
        String body = "{\"username\":\"\",\"email\":\"no-es-email\",\"password\":\"123\"}";
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }
}
