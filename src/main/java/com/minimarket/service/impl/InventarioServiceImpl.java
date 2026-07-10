package com.minimarket.service.impl;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.InventarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository,
                                 ProductoRepository productoRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    @Override
    public Inventario findById(Long id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    @Override
    public Inventario save(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @Override
    public void deleteById(Long id) {
        inventarioRepository.deleteById(id);
    }

    @Override
    public List<Inventario> findByProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    @Override
    public Inventario registrarMovimiento(Inventario inventario) {
        Producto producto = inventario.getProducto();
        if (producto == null) {
            throw new IllegalArgumentException("El movimiento debe estar asociado a un producto");
        }
        if (inventario.getCantidad() == null || inventario.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad del movimiento debe ser mayor que cero");
        }
        String tipo = inventario.getTipoMovimiento();
        int stockActual = producto.getStock() == null ? 0 : producto.getStock();

        if ("Entrada".equalsIgnoreCase(tipo)) {
            producto.setStock(stockActual + inventario.getCantidad());
        } else if ("Salida".equalsIgnoreCase(tipo)) {
            if (inventario.getCantidad() > stockActual) {
                throw new IllegalStateException("Stock insuficiente para registrar la salida");
            }
            producto.setStock(stockActual - inventario.getCantidad());
        } else {
            throw new IllegalArgumentException("Tipo de movimiento invalido: debe ser 'Entrada' o 'Salida'");
        }
        productoRepository.save(producto);
        return inventarioRepository.save(inventario);
    }
}
