package com.minimarket.service;

import com.minimarket.entity.Carrito;

import java.util.List;

public interface CarritoService {
    List<Carrito> findAll();
    Carrito findById(Long id);
    Carrito save(Carrito carrito);
    void deleteById(Long id);
    List<Carrito> findByUsuarioId(Long usuarioId);

    /**
     * Agrega un producto al carrito validando que exista stock suficiente.
     * Lanza IllegalArgumentException si el producto es nulo o IllegalStateException
     * si la cantidad solicitada supera el stock disponible.
     */
    Carrito agregarProducto(Carrito carrito);
}
