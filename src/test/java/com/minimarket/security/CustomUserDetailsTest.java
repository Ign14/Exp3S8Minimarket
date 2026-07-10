package com.minimarket.security;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.security.model.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** Verifica el mapeo de roles a authorities (prefijo ROLE_) y el estado de la cuenta. */
class CustomUserDetailsTest {

    @Test
    @DisplayName("Las authorities llevan el prefijo ROLE_ a partir del nombre del rol")
    void testAuthoritiesConPrefijo() {
        Usuario u = new Usuario();
        u.setUsername("admin");
        u.setPassword("hash");
        u.setActivo(true);
        u.setRoles(Set.of(new Rol("ADMINISTRADOR")));

        CustomUserDetails details = new CustomUserDetails(u);

        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR")));
        assertTrue(details.isEnabled());
    }

    @Test
    @DisplayName("Una cuenta inactiva queda deshabilitada (isEnabled = false)")
    void testCuentaInactiva() {
        Usuario u = new Usuario();
        u.setUsername("inactivo");
        u.setPassword("hash");
        u.setActivo(false);
        u.setRoles(Set.of(new Rol("CLIENTE")));

        assertFalse(new CustomUserDetails(u).isEnabled());
    }
}
