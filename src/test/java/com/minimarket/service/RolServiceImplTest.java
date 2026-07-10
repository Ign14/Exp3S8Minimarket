package com.minimarket.service;

import com.minimarket.entity.Rol;
import com.minimarket.repository.RolRepository;
import com.minimarket.service.impl.RolServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Pruebas del servicio de Rol con Mockito. */
@ExtendWith(MockitoExtension.class)
class RolServiceImplTest {

    @Mock
    private RolRepository rolRepository;
    @InjectMocks
    private RolServiceImpl rolService;

    @Test
    @DisplayName("Buscar rol existente por nombre lo retorna")
    void testRolExistente() {
        when(rolRepository.findByNombre("CAJERO")).thenReturn(Optional.of(new Rol("CAJERO")));
        Optional<Rol> r = rolService.findByNombre("CAJERO");
        assertTrue(r.isPresent());
        assertEquals("CAJERO", r.get().getNombre());
    }

    @Test
    @DisplayName("Buscar rol inexistente retorna vacio")
    void testRolInexistente() {
        when(rolRepository.findByNombre("XX")).thenReturn(Optional.empty());
        assertTrue(rolService.findByNombre("XX").isEmpty());
    }
}
