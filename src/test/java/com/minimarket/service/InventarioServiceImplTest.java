package com.minimarket.service;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.InventarioServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Pruebas de inventario: movimientos asociados a producto y ajuste de stock. */
@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Inventario movimiento(Producto p, String tipo, int cantidad) {
        Inventario inv = new Inventario();
        inv.setProducto(p);
        inv.setTipoMovimiento(tipo);
        inv.setCantidad(cantidad);
        return inv;
    }

    private Producto producto(int stock) {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Fideos");
        p.setStock(stock);
        return p;
    }

    @Test
    @DisplayName("Movimiento de Entrada incrementa el stock del producto")
    void testEntradaIncrementaStock() {
        Producto p = producto(10);
        Inventario inv = movimiento(p, "Entrada", 5);
        when(inventarioRepository.save(inv)).thenReturn(inv);

        inventarioService.registrarMovimiento(inv);

        assertEquals(15, p.getStock());
        verify(productoRepository).save(p);
        verify(inventarioRepository).save(inv);
    }

    @Test
    @DisplayName("Movimiento de Salida disminuye el stock del producto")
    void testSalidaDisminuyeStock() {
        Producto p = producto(10);
        Inventario inv = movimiento(p, "Salida", 4);
        when(inventarioRepository.save(inv)).thenReturn(inv);

        inventarioService.registrarMovimiento(inv);

        assertEquals(6, p.getStock());
    }

    @Test
    @DisplayName("Salida mayor al stock disponible lanza IllegalStateException")
    void testSalidaSinStock() {
        Producto p = producto(3);
        Inventario inv = movimiento(p, "Salida", 10);

        assertThrows(IllegalStateException.class, () -> inventarioService.registrarMovimiento(inv));
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Movimiento sin producto asociado lanza IllegalArgumentException")
    void testMovimientoSinProducto() {
        Inventario inv = movimiento(null, "Entrada", 5);
        assertThrows(IllegalArgumentException.class, () -> inventarioService.registrarMovimiento(inv));
    }

    @Test
    @DisplayName("Tipo de movimiento invalido lanza IllegalArgumentException")
    void testTipoInvalido() {
        Producto p = producto(10);
        Inventario inv = movimiento(p, "Otro", 5);
        assertThrows(IllegalArgumentException.class, () -> inventarioService.registrarMovimiento(inv));
    }
}
