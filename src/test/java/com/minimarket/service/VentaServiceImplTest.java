package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Pruebas de venta: solo se confirma si hay stock suficiente en todos los detalles. */
@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private DetalleVenta detalle(String nombre, int stock, int cantidad) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setStock(stock);
        DetalleVenta d = new DetalleVenta();
        d.setProducto(p);
        d.setCantidad(cantidad);
        d.setPrecio(990.0);
        return d;
    }

    @Test
    @DisplayName("Registrar venta con stock suficiente la guarda correctamente")
    void testRegistrarVentaExitosa() {
        Venta venta = new Venta();
        venta.setDetalles(List.of(detalle("Arroz", 10, 2), detalle("Leche", 5, 1)));
        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.registrarVenta(venta);

        assertNotNull(resultado);
        assertEquals(2, resultado.getDetalles().size());
        verify(ventaRepository).save(venta);
    }

    @Test
    @DisplayName("Registrar venta con stock insuficiente lanza IllegalStateException")
    void testRegistrarVentaSinStock() {
        Venta venta = new Venta();
        venta.setDetalles(List.of(detalle("Arroz", 1, 5)));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> ventaService.registrarVenta(venta));
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
        verify(ventaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Registrar venta sin detalles lanza IllegalArgumentException")
    void testRegistrarVentaSinDetalles() {
        Venta venta = new Venta();
        assertThrows(IllegalArgumentException.class, () -> ventaService.registrarVenta(venta));
        verify(ventaRepository, never()).save(any());
    }
}
