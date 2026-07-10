package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.repository.DetalleVentaRepository;
import com.minimarket.service.impl.DetalleVentaServiceImpl;
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

/** Pruebas del servicio de DetalleVenta con Mockito. */
@ExtendWith(MockitoExtension.class)
class DetalleVentaServiceImplTest {

    @Mock
    private DetalleVentaRepository detalleVentaRepository;
    @InjectMocks
    private DetalleVentaServiceImpl detalleVentaService;

    private DetalleVenta det(Long id, int cantidad) {
        DetalleVenta d = new DetalleVenta();
        d.setId(id);
        d.setCantidad(cantidad);
        d.setPrecio(500.0);
        return d;
    }

    @Test
    @DisplayName("Guardar detalle delega en el repositorio")
    void testGuardar() {
        DetalleVenta d = det(1L, 2);
        when(detalleVentaRepository.save(d)).thenReturn(d);
        assertEquals(2, detalleVentaService.save(d).getCantidad());
    }

    @Test
    @DisplayName("Buscar detalles por venta retorna la lista")
    void testPorVenta() {
        when(detalleVentaRepository.findByVentaId(5L)).thenReturn(List.of(det(1L, 1), det(2L, 3)));
        assertEquals(2, detalleVentaService.findByVentaId(5L).size());
    }

    @Test
    @DisplayName("Buscar por ID inexistente retorna null")
    void testBuscarInexistente() {
        when(detalleVentaRepository.findById(9L)).thenReturn(Optional.empty());
        assertNull(detalleVentaService.findById(9L));
    }

    @Test
    @DisplayName("Eliminar y listar")
    void testEliminarListar() {
        detalleVentaService.deleteById(1L);
        verify(detalleVentaRepository).deleteById(1L);
        when(detalleVentaRepository.findAll()).thenReturn(List.of(det(1L, 1)));
        assertEquals(1, detalleVentaService.findAll().size());
    }
}
