package com.minimarket.security;

import com.minimarket.security.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas unitarias de JwtUtil: generacion, validacion y expiracion del token. */
class JwtUtilTest {

    private static final String SECRET = "ClaveDePruebaParaJwtUtilDeMiniMarketPlus_2025_32+chars";
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // Expiracion de 1 hora para las pruebas normales.
        jwtUtil = new JwtUtil(SECRET, 3600000L);
    }

    @Test
    @DisplayName("El token generado permite extraer el username original")
    void testGenerarYExtraerUsername() {
        String token = jwtUtil.generarToken("admin", List.of("ADMINISTRADOR"));
        assertNotNull(token);
        assertEquals("admin", jwtUtil.extraerUsername(token));
    }

    @Test
    @DisplayName("Un token valido y no expirado pasa la validacion")
    void testTokenValido() {
        String token = jwtUtil.generarToken("cajero", List.of("CAJERO"));
        assertTrue(jwtUtil.validarToken(token, "cajero"));
    }

    @Test
    @DisplayName("Un token alterado no es valido")
    void testTokenAlterado() {
        String token = jwtUtil.generarToken("cajero", List.of("CAJERO")) + "xxxx";
        assertFalse(jwtUtil.validarToken(token, "cajero"));
    }

    @Test
    @DisplayName("Un token expirado se detecta como invalido")
    void testTokenExpirado() throws InterruptedException {
        JwtUtil jwtCorto = new JwtUtil(SECRET, 1L); // expira casi de inmediato
        String token = jwtCorto.generarToken("cliente", List.of("CLIENTE"));
        Thread.sleep(20);
        assertFalse(jwtCorto.validarToken(token, "cliente"));
    }
}
