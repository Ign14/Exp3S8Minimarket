package com.minimarket.service;

import com.minimarket.entity.Categoria;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.service.impl.CategoriaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Pruebas del servicio de Categoria con Mockito. */
@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;
    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria cat(Long id, String nombre) {
        Categoria c = new Categoria();
        c.setId(id);
        c.setNombre(nombre);
        return c;
    }

    @Test
    @DisplayName("Guardar categoria delega en el repositorio")
    void testGuardar() {
        Categoria c = cat(1L, "Bebidas");
        when(categoriaRepository.save(c)).thenReturn(c);
        assertEquals("Bebidas", categoriaService.save(c).getNombre());
        verify(categoriaRepository).save(c);
    }

    @Test
    @DisplayName("Listar categorias retorna la lista")
    void testListar() {
        when(categoriaRepository.findAll()).thenReturn(List.of(cat(1L, "Abarrotes"), cat(2L, "Lacteos")));
        assertEquals(2, categoriaService.findAll().size());
    }

    @Test
    @DisplayName("Buscar por ID existente / inexistente")
    void testBuscar() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(cat(1L, "Limpieza")));
        when(categoriaRepository.findById(9L)).thenReturn(Optional.empty());
        assertNotNull(categoriaService.findById(1L));
        assertNull(categoriaService.findById(9L));
    }

    @Test
    @DisplayName("Eliminar categoria invoca deleteById")
    void testEliminar() {
        categoriaService.deleteById(3L);
        verify(categoriaRepository).deleteById(3L);
    }
}
