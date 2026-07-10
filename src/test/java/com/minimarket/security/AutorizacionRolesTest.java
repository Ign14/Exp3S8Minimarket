package com.minimarket.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de autorizacion por rol usando MockMvc y usuarios simulados.
 * Verifican que las restricciones del caso se cumplan (cliente, cajero, administrador).
 */
@SpringBootTest
@AutoConfigureMockMvc
class AutorizacionRolesTest {

    @Autowired
    private MockMvc mockMvc;

    // ---- /api/usuarios : solo ADMINISTRADOR ----

    @Test
    @DisplayName("Sin token, /api/usuarios responde 401 (no autenticado)")
    void usuariosSinToken() throws Exception {
        mockMvc.perform(get("/api/usuarios")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    @DisplayName("Un CLIENTE en /api/usuarios recibe 403 (sin permisos)")
    void usuariosComoCliente() throws Exception {
        mockMvc.perform(get("/api/usuarios")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("Un ADMINISTRADOR accede a /api/usuarios (200)")
    void usuariosComoAdmin() throws Exception {
        mockMvc.perform(get("/api/usuarios")).andExpect(status().isOk());
    }

    // ---- Modificar productos : solo ADMINISTRADOR ----

    @Test
    @WithMockUser(roles = "CAJERO")
    @DisplayName("Un CAJERO no puede eliminar productos (403)")
    void eliminarProductoComoCajero() throws Exception {
        mockMvc.perform(delete("/api/productos/99999")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("Un ADMINISTRADOR puede operar sobre productos (no recibe 403)")
    void eliminarProductoComoAdmin() throws Exception {
        // Producto inexistente -> 404, pero lo importante es que NO sea 403.
        mockMvc.perform(delete("/api/productos/99999")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("La consulta de productos (GET) es publica (200)")
    void listarProductosPublico() throws Exception {
        mockMvc.perform(get("/api/productos")).andExpect(status().isOk());
    }

    // ---- /api/inventario : CAJERO o ADMINISTRADOR ----

    @Test
    @WithMockUser(roles = "CLIENTE")
    @DisplayName("Un CLIENTE no puede ver inventario (403)")
    void inventarioComoCliente() throws Exception {
        mockMvc.perform(get("/api/inventario")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CAJERO")
    @DisplayName("Un CAJERO puede ver inventario (200)")
    void inventarioComoCajero() throws Exception {
        mockMvc.perform(get("/api/inventario")).andExpect(status().isOk());
    }

    // ---- /api/ventas : generar solo CAJERO/ADMINISTRADOR ----

    @Test
    @WithMockUser(roles = "CLIENTE")
    @DisplayName("Un CLIENTE no puede acceder a ventas (403)")
    void ventasComoCliente() throws Exception {
        mockMvc.perform(get("/api/ventas")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CAJERO")
    @DisplayName("Un CAJERO puede acceder a ventas (200)")
    void ventasComoCajero() throws Exception {
        mockMvc.perform(get("/api/ventas")).andExpect(status().isOk());
    }
}
