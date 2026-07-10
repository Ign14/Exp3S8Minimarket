package com.minimarket.service;

import com.minimarket.entity.Inventario;

import java.util.List;

public interface InventarioService {
    List<Inventario> findAll();
    Inventario findById(Long id);
    Inventario save(Inventario inventario);
    void deleteById(Long id);
    List<Inventario> findByProductoId(Long productoId);

    /**
     * Registra un movimiento de inventario (Entrada o Salida) validando que
     * este asociado a un producto y actualizando el stock del producto.
     * Lanza IllegalArgumentException si no hay producto asociado e
     * IllegalStateException si una salida supera el stock disponible.
     */
    Inventario registrarMovimiento(Inventario inventario);
}
