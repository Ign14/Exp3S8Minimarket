package com.minimarket.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de la entidad Venta y su detalle: una venta refleja sus productos. */
class VentaTest {

    @Test
    @DisplayName("Una venta registra correctamente sus detalles (productos, cantidad y precio)")
    void testVentaConDetalles() {
        Producto p = new Producto();
        p.setNombre("Fideos");
        p.setPrecio(890.0);

        DetalleVenta d = new DetalleVenta();
        d.setProducto(p);
        d.setCantidad(3);
        d.setPrecio(890.0);

        Usuario cajero = new Usuario();
        cajero.setUsername("cajero");

        Venta venta = new Venta();
        venta.setId(1L);
        venta.setUsuario(cajero);
        venta.setFecha(new Date());
        venta.setDetalles(List.of(d));

        assertEquals(1, venta.getDetalles().size());
        assertEquals("Fideos", venta.getDetalles().get(0).getProducto().getNombre());
        assertEquals(3, venta.getDetalles().get(0).getCantidad());
        assertEquals(890.0, venta.getDetalles().get(0).getPrecio());
        assertEquals("cajero", venta.getUsuario().getUsername());
    }
}
