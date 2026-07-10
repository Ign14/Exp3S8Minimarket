package com.minimarket.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de la entidad Inventario: movimiento asociado a un producto. */
class InventarioTest {

    @Test
    @DisplayName("Crear movimiento de inventario asociado a un producto")
    void testCrearMovimiento() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Leche");

        Inventario inv = new Inventario();
        inv.setId(5L);
        inv.setProducto(p);
        inv.setCantidad(20);
        inv.setTipoMovimiento("Entrada");
        inv.setFechaMovimiento(new Date());

        assertEquals(5L, inv.getId());
        assertEquals("Leche", inv.getProducto().getNombre());
        assertEquals(20, inv.getCantidad());
        assertEquals("Entrada", inv.getTipoMovimiento());
        assertNotNull(inv.getFechaMovimiento());
    }
}
