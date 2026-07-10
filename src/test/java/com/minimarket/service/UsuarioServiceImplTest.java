package com.minimarket.service;

import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Pruebas del servicio de Usuario (busqueda por username: caso valido e invalido). */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    @DisplayName("Buscar usuario existente por username lo retorna")
    void testBuscarUsuarioExistente() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setUsername("admin");
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(u));

        Optional<Usuario> resultado = usuarioService.findByUsername("admin");

        assertTrue(resultado.isPresent());
        assertEquals("admin", resultado.get().getUsername());
    }

    @Test
    @DisplayName("Buscar usuario inexistente retorna Optional vacio")
    void testBuscarUsuarioInexistente() {
        when(usuarioRepository.findByUsername("fantasma")).thenReturn(Optional.empty());
        assertTrue(usuarioService.findByUsername("fantasma").isEmpty());
    }

    @Test
    @DisplayName("Guardar usuario delega en el repositorio")
    void testGuardarUsuario() {
        Usuario u = new Usuario();
        u.setUsername("nuevo");
        when(usuarioRepository.save(u)).thenReturn(u);
        assertEquals("nuevo", usuarioService.save(u).getUsername());
        verify(usuarioRepository).save(u);
    }
}
