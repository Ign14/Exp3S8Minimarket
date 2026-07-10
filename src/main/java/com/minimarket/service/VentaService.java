package com.minimarket.service;

import com.minimarket.entity.Venta;

import java.util.List;

public interface VentaService {
    List<Venta> findAll();
    Venta findById(Long id);
    Venta save(Venta venta);
    List<Venta> findByUsuarioId(Long usuarioId);

    /**
     * Registra una venta solo si todos sus detalles tienen stock suficiente.
     * Lanza IllegalArgumentException si la venta no tiene detalles e
     * IllegalStateException si algun producto no tiene stock suficiente.
     */
    Venta registrarVenta(Venta venta);
}
