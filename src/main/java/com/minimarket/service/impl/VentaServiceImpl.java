package com.minimarket.service.impl;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.VentaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta findById(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> findByUsuarioId(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Venta registrarVenta(Venta venta) {
        List<DetalleVenta> detalles = venta.getDetalles();
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un detalle");
        }
        for (DetalleVenta detalle : detalles) {
            if (detalle.getProducto() == null) {
                throw new IllegalArgumentException("Cada detalle debe tener un producto asociado");
            }
            Integer stock = detalle.getProducto().getStock();
            if (stock == null || detalle.getCantidad() > stock) {
                throw new IllegalStateException(
                        "Stock insuficiente para el producto: " + detalle.getProducto().getNombre());
            }
        }
        return ventaRepository.save(venta);
    }
}
