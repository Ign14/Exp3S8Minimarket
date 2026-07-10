package com.minimarket.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de la entidad Producto: datos y relacion con categoria. */
class ProductoTest {

    @Test
    @DisplayName("Crear producto y verificar sus datos")
    void testCrearProducto() {
        Categoria cat = new Categoria();
        cat.setId(1L);
        cat.setNombre("Abarrotes");

        Producto p = new Producto();
        p.setId(10L);
        p.setNombre("Arroz 1kg");
        p.setPrecio(1290.0);
        p.setStock(100);
        p.setCategoria(cat);

        assertEquals(10L, p.getId());
        assertEquals("Arroz 1kg", p.getNombre());
        assertEquals(1290.0, p.getPrecio());
        assertEquals(100, p.getStock());
        assertEquals("Abarrotes", p.getCategoria().getNombre());
    }

    @Test
    @DisplayName("Actualizar el stock del producto refleja el nuevo valor")
    void testActualizarStock() {
        Producto p = new Producto();
        p.setStock(50);
        p.setStock(p.getStock() - 10);
        assertEquals(40, p.getStock());
    }
}
