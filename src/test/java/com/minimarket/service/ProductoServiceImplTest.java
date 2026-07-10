package com.minimarket.service;

import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.ProductoServiceImpl;
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

/** Pruebas unitarias del servicio de Producto con Mockito (sin base de datos real). */
@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto(Long id, String nombre, int stock) {
        Producto p = new Producto();
        p.setId(id);
        p.setNombre(nombre);
        p.setPrecio(1000.0);
        p.setStock(stock);
        return p;
    }

    @Test
    @DisplayName("Guardar producto delega en el repositorio y retorna el producto")
    void testGuardarProducto() {
        Producto p = producto(1L, "Arroz", 50);
        when(productoRepository.save(p)).thenReturn(p);

        Producto resultado = productoService.save(p);

        assertNotNull(resultado);
        assertEquals("Arroz", resultado.getNombre());
        verify(productoRepository, times(1)).save(p);
    }

    @Test
    @DisplayName("Buscar producto por ID existente retorna el producto")
    void testBuscarPorIdExistente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto(1L, "Leche", 20)));

        Producto resultado = productoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Buscar producto por ID inexistente retorna null")
    void testBuscarPorIdInexistente() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        assertNull(productoService.findById(99L));
    }

    @Test
    @DisplayName("Listar productos retorna la lista del repositorio")
    void testListarProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(producto(1L, "Pan", 10), producto(2L, "Agua", 30)));
        assertEquals(2, productoService.findAll().size());
    }

    @Test
    @DisplayName("Eliminar producto invoca deleteById del repositorio")
    void testEliminarProducto() {
        productoService.deleteById(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }
}
