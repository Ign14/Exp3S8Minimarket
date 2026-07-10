package com.minimarket.service;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.service.impl.CarritoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Pruebas del carrito: validacion de stock (escenarios de exito y error). */
@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Carrito carrito(int cantidad, Integer stockProducto) {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Bebida");
        p.setStock(stockProducto);
        Carrito c = new Carrito();
        c.setProducto(p);
        c.setCantidad(cantidad);
        return c;
    }

    @Test
    @DisplayName("Agregar al carrito con stock suficiente guarda el carrito")
    void testAgregarConStockSuficiente() {
        Carrito c = carrito(3, 10);
        when(carritoRepository.save(c)).thenReturn(c);

        Carrito resultado = carritoService.agregarProducto(c);

        assertNotNull(resultado);
        verify(carritoRepository).save(c);
    }

    @Test
    @DisplayName("Agregar al carrito con stock insuficiente lanza IllegalStateException")
    void testAgregarConStockInsuficiente() {
        Carrito c = carrito(15, 10);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> carritoService.agregarProducto(c));
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
        verify(carritoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Agregar al carrito sin producto lanza IllegalArgumentException")
    void testAgregarSinProducto() {
        Carrito c = new Carrito();
        c.setCantidad(1);
        assertThrows(IllegalArgumentException.class, () -> carritoService.agregarProducto(c));
        verify(carritoRepository, never()).save(any());
    }
}
